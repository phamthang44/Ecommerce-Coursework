package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.dto.request.CartItemRequestDTO;
import com.greenwich.ecommerce.dto.request.CartItemUpdateRequestDTO;
import com.greenwich.ecommerce.dto.request.CartRequestDeleteItemsDTO;
import com.greenwich.ecommerce.dto.response.CartItemResponseDTO;
import com.greenwich.ecommerce.dto.response.CartResponseDTO;
import com.greenwich.ecommerce.entity.*;
import com.greenwich.ecommerce.exception.BadRequestException;
import com.greenwich.ecommerce.exception.InvalidDataException;
import com.greenwich.ecommerce.exception.ResourceNotFoundException;
import com.greenwich.ecommerce.exception.UnauthorizedException;
import com.greenwich.ecommerce.repository.*;
import com.greenwich.ecommerce.service.CartService;
import com.greenwich.ecommerce.service.ProductService;
import com.greenwich.ecommerce.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final CartItemRepository cartItemRepository;
    private final CartServiceValidator cartServiceValidator;
    private final AssetService assetService;
    private final UserService userService;

    @Override
    @Transactional
    public CartResponseDTO addProductToCart(CartItemRequestDTO cartItemRequestDTO, Long userId) {

        Long productId = cartItemRequestDTO.getProductId();
        int quantity = cartItemRequestDTO.getQuantity();

        cartServiceValidator.validateAddToCartRequest(userId, productId, quantity); // for sure that will not trick to fetch request with quantity = 0 or negative value
        log.info("Adding product with id {} to cart for user {}", productId, userId);

        Cart cart = getOrCreateCart(userId);
        Product product = productService.getProductEntityById(productId);

        if (product.getStockQuantity() == 0) {
            log.error("Product with id {} is out of stock", productId);
            throw new ResourceNotFoundException(product.getName() + " is out of stock");
        }

        if (quantity > product.getStockQuantity()) {
            log.error("Product with id {} has insufficient stock. Available: {}, Requested: {}", productId, product.getStockQuantity(), quantity);
            throw new BadRequestException("Insufficient stock for product : " + product.getName());
        }
        CartItem existingCartItem = findCartItemByProduct(cart, productId);

        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getQuantity() + quantity;
            existingCartItem.setQuantity(newQuantity);
            log.info("Product with id {} already exists in the cart, updated quantity to {}", productId, quantity);
        }  else {
            CartItem newCartItem = new CartItem(cart, product.getCategory(), product, quantity);
            cart.getCartItems().add(newCartItem);
            log.info("Added new product with id {} to cart", productId);
        }
        cart.setTotalPrice(getCartTotalPrice(cart));
        return getCartResponseDTO(cartRepository.save(cart));
    }

    private CartResponseDTO getCartResponseDTO(Cart cart) {
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setCartItems(cart.getCartItems().stream()
                .filter(item -> item.getCategory() != null && item.getProduct() != null)
                .map(cartItem -> CartItemResponseDTO.builder()
                        .id(cartItem.getId())
                        .productId(cartItem.getProduct().getId())
                        .name(cartItem.getProduct().getName())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getProduct().getPrice())
                        .subTotalPrice(getSubTotalPrice(cartItem))
                        .assetUrl(getCartItemAssetUrl(cartItem))
                        .build())
                .toList());
        BigDecimal totalPrice = getCartTotalPrice(cart);
        cartResponseDTO.setTotalPrice(totalPrice);

        return cartResponseDTO;
    }

    private String getCartItemAssetUrl(CartItem cartItem) {
        if (cartItem.getProduct() != null && cartItem.getProduct().getAssets() != null && !cartItem.getProduct().getAssets().isEmpty()) {

            Asset asset = assetService.getAssetByUsageId(cartItem.getProduct().getId());
            if (asset == null) {
                log.warn("No asset found for product with id: {}", cartItem.getProduct().getId());
                return assetService.getAssetById(2L).getUrl(); // or a default image URL
            }
            return asset.getUrl();
        }
        return null; // or a default image URL
    }

    private CartItem findCartItemByProduct(Cart cart, Long productId) {
        return cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    private BigDecimal getSubTotalPrice(CartItem cartItem) {
        if (cartItem.getProduct() != null) {
            return cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        }
        return BigDecimal.ZERO; // or handle as needed
    }

    private BigDecimal getCartTotalPrice(Cart cart) {
        return cart.getCartItems().stream()
                .map(this::getSubTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isCartBelongToUser(Long userId, Cart cart) {
        return cartRepository.existsByIdAndUserId(cart.getId(), userId);
    }

    private Cart createCart(Long userId) {
        if (userId == null || userId <= 0) {
            log.error("Invalid user id: {}", userId);
            throw new InvalidDataException("User id must be greater than zero");
        }

        Cart cart = new Cart();
        User user = userService.getUserById(userId);

        cart.setUser(user);
        cart.setStatus("Active");
        return cartRepository.save(cart);
    }

    private Cart getOrCreateCart(Long userId) {
        Cart cart = cartRepository.getByUserId(userId);
        if (cart == null) {
            log.info("No cart found for user id: {}, creating a new one", userId);
            return createCart(userId);
        }
        return cart;
    }

    @Override
    public CartResponseDTO getCartByUserId(Long userId) {

        cartServiceValidator.validateViewCartRequest(userId);
        log.info("Getting cart for user id: {}", userId);

        Cart cart = getOrCreateCart(userId);
        return getCartResponseDTO(cart);

    }

    private boolean isCartItemBelongToUser(Long userId, CartItem cartItem) {
        return cartItemRepository.existsByIdAndCartUserId(cartItem.getId(), userId);
    }

    @Transactional
    @Override
    public CartResponseDTO changeCartItemQuantity(CartItemUpdateRequestDTO updateDTO, Long userId) {
        Long cartItemId = updateDTO.getCartItemId();
        Integer quantity = updateDTO.getQuantity();

        cartServiceValidator.validateChangeCartItemQuantityRequest(userId, cartItemId, quantity);
        log.info("Changing quantity of product {} in cart for user {} to {}", cartItemId, userId, quantity);

        Cart cart = getOrCreateCart(userId);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> {;
            log.error("Change cart item quantity : Cart item not found with id: {}", cartItemId);
            return new ResourceNotFoundException("Cart item not found with id: " + cartItemId);
        });
        if (!isCartItemBelongToUser(userId, cartItem)) {
            log.error("Change cart item quantity : Cart item with id {} does not belong to user {}", cartItemId, userId);
            throw new UnauthorizedException("You do not have permission to change this cart item");
        }

        Product product = cartItem.getProduct();
        if (product == null) {
            log.error("Change cart item quantity : Product is null for cartItem {}", cartItemId);
            throw new ResourceNotFoundException("Product may be out of stock or deleted");
        }
        //        Check the quantity of the product
        if (product.getStockQuantity() < quantity) {
            log.error("Cart item with id {} has insufficient stock. Available: {}, Requested: {}", cartItemId, product.getStockQuantity(), quantity);
            throw new BadRequestException("Insufficient stock for product : " + product.getName());
        }
        if (product.getStockQuantity() == quantity) {
            log.error("Cart item with id {} has the same quantity as stock. Available: {}, Requested: {}", cartItemId, product.getStockQuantity(), quantity);
            throw new BadRequestException("You cannot set the quantity to the same as stock for product : " + product.getName());
        }
//        Category category = product.getCategory();

        cartItem.setQuantity(quantity); // Managed entity, change will be persisted
        cartItemRepository.save(cartItem);
        for (CartItem item : cart.getCartItems()) {
            if (item.getId().equals(cartItemId)) {
                item.setQuantity(quantity);
            }
        }
        cart.setTotalPrice(getCartTotalPrice(cart));
        Cart updatedCart = cartRepository.save(cart);
        return getCartResponseDTO(updatedCart);
    }

    @Override
    @Transactional
    public CartResponseDTO removeCartItemFromCart(Long cartItemId, Long userId) {
        cartServiceValidator.validateDeleteCartItemRequest(userId, cartItemId);

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> {
            log.error("Remove cart item : Cart item not found with id: {}", cartItemId);
            return new ResourceNotFoundException("Cart item not found with id: " + cartItemId);
        });
        if (!isCartItemBelongToUser(userId, cartItem)) {
            log.error("Remove cart item : Cart item with id {} does not belong to user {}", cartItemId, userId);
            throw new UnauthorizedException("You do not have permission to delete this cart item");
        }
        log.info("Removing cart item:  with id {} from cart for user {}", cartItemId, userId);
        Cart cart = cartItem.getCart();
        cartItem.setCategory(null);
        cart.getCartItems().remove(cartItem);

        Cart updatedCart = cartRepository.findByUserIdWithItems(userId) // Fetch the cart again to ensure it is up-to-date
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        return getCartResponseDTO(updatedCart);
    }

    @Override
    @Transactional
    public CartResponseDTO removeCartItemsFromCart(CartRequestDeleteItemsDTO items, Long userId) {
        cartServiceValidator.validateDeleteCartItemsRequest(userId, items);
//        boolean isCartBelongToUser = cartRepository.existsByIdAndUserId(userId);
        log.info("Removing cart items: {} from cart for user {}", items.getCartItemIds().toString(), userId);

        List<CartItem> cartItems = cartItemRepository.findAllById(items.getCartItemIds());
        for (CartItem item : cartItems) {
            if (!isCartItemBelongToUser(userId, item)) {
                log.error("Remove cart items : Cart item with id {} does not belong to user {}", item.getId(), userId);
                throw new UnauthorizedException("You do not have permission to delete this cart item");
            }
        }

        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));

        //current cartItems list


        List<CartItem> itemsToRemove = new ArrayList<>(
                cart.getCartItems().stream()
                        .filter(ci -> items.getCartItemIds().contains(ci.getId()))
                        .toList()
        );

        for (CartItem ci : itemsToRemove) {
            log.info("Removing cart item with id: {}", ci.getId());
            cart.getCartItems().remove(ci);
            ci.setCart(null);
            ci.setCategory(null);
        }

        cartRepository.save(cart); // force flush

        Cart updatedCart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        return getCartResponseDTO(updatedCart);
    }

    @Override
    @Transactional
    public CartResponseDTO removeAllItemsFromCart(Long userId) {

        cartServiceValidator.validateViewCartRequest(userId);

        Cart cart = getOrCreateCart(userId);
        log.info("Cancel cart :  Removing all items from cart for user {}", userId);
        if (cart.getCartItems().isEmpty()) {
            log.info("Cancel cart :  Cart is already empty for user {}", userId);
            return getCartResponseDTO(cart);
        }
        List<CartItem> itemsToRemove = new ArrayList<>(cart.getCartItems());

        for (CartItem ci : itemsToRemove) {
            if (!isCartItemBelongToUser(userId, ci)) {
                log.error("Remove all items in cart: Cart item with id {} does not belong to user {}", ci.getId(), userId);
                throw new UnauthorizedException("You do not have permission to delete this cart item");
            }
            cart.getCartItems().remove(ci); // remove from list
            ci.setCart(null);               // mark orphan
            ci.setCategory(null);           //remove category reference
        }
        cart.getCartItems().clear();
        log.info("Cancel cart : All items removed from cart for user {}", userId);
        return getCartResponseDTO(cart);
    }
}
