package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.dto.request.OrderItemRequestDTO;
import com.greenwich.ecommerce.dto.request.OrderRequestDTO;
import com.greenwich.ecommerce.dto.response.OrderItemResponseDTO;
import com.greenwich.ecommerce.dto.response.OrderResponseDTO;
import com.greenwich.ecommerce.entity.*;
import com.greenwich.ecommerce.exception.UnauthorizedException;
import com.greenwich.ecommerce.repository.AddressRepository;
import com.greenwich.ecommerce.repository.OrderRepository;
import com.greenwich.ecommerce.repository.OrderItemRepository;
import com.greenwich.ecommerce.repository.CartItemRepository;
import com.greenwich.ecommerce.service.CartService;
import com.greenwich.ecommerce.service.CategoryService;
import com.greenwich.ecommerce.service.UserService;
import com.greenwich.ecommerce.service.impl.OrderServiceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderServiceImpl {
    private final OrderRepository orderRepository;
    private final CategoryService categoryService;
    private final AssetService assetService;
    private final UserService userService;
    private final CartService cartService;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final CartServiceImpl cartServiceImpl;
    private final OrderServiceValidator orderServiceValidator;


    // lay tat ca order cua 1 thang user
    public OrderResponseDTO getOrderByUserId(Long userId) {
        log.info("Fetching order with user ID: {}", userId);
        return null;
    }

    public OrderResponseDTO getOrderById(Long orderId) {
        log.info("Fetching order with ID: {}", orderId);
        orderServiceValidator.validateOrderId(orderId);

        Order order = orderRepository.getOrderById(orderId);

        return getOrderResponseDTO(order);
    }

    private OrderResponseDTO getOrderResponseDTO(Order order) {
        OrderResponseDTO orderResponse = new OrderResponseDTO(); // nay khi nao dung moi khai bao
        orderResponse.setOrderItems(order.getOrderItems().stream()
                .map(orderItem ->OrderItemResponseDTO.builder()
                        .orderId(order.getId())
                        .id(orderItem.getId())
                        .productId(orderItem.getProduct().getId())
                        .name(orderItem.getProduct().getName())
                        .quantity(orderItem.getQuantity())
                        .price(orderItem.getProduct().getPrice())
                        .subTotalPrice(orderItem.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                        .assetUrl(getOrderItemAssetUrl(orderItem))
                        .build())
        .toList());
        orderResponse.setTotalPrice(order.getTotalPrice());
        return orderResponse;
    }

    private String getOrderItemAssetUrl(OrderItem orderitem) {
        if (orderitem.getProduct() != null && orderitem.getProduct().getAssets() != null && !orderitem.getProduct().getAssets().isEmpty()) {

            Asset asset = assetService.getAssetByUsageId(orderitem.getProduct().getId());

            return asset.getUrl();
        }
        return null;
    }

    public OrderResponseDTO createOrderWithAllItems(Long userId) {
        orderServiceValidator.validateUserId(userId);
        log.info("Making order for user id: {}", userId);

        Cart cart = cartServiceImpl.getOrCreateCart(userId);

        if (cart == null) {
            log.error("Cart not found for user id: {}", userId);
            throw new RuntimeException("Cart not found for user id: " + userId);
        }

        Order order = new Order();
        order.setUser(userService.getUserById(userId));
        order.setOrderItems(new ArrayList<>());

        Category category = categoryService.getCategoryEntityById(1L);

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            log.error("Invalid order request - the cart is empty");
            throw new IllegalArgumentException("Invalid order request - the cart empty");
        }

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            log.info("Adding product with id {} to cart for user {}", cartItem.getId(), userId);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setCategory(category);
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(totalPrice);

        setOrderDetail(order);

        return getOrderResponseDTO(orderRepository.save(order));
    }

    // nếu customer kh phải người sở hữu thì kh làm gì được
    // Phải có vailidate chỗ này

    public OrderResponseDTO createOrderWithSelectedItems(OrderItemRequestDTO items ,Long userId) {
        orderServiceValidator.validateOrderItemRequest(userId, items);
        for (Long cartItemId : items.getCartItemIds()) {
            if (!isCartItemBelongToUser(cartItemId, userId)) {
                log.error("Cart item with id {} does not belong to user {}", cartItemId, userId);
                throw new UnauthorizedException("You are not allowed to use cart item id: " + cartItemId);
            }
        }
        log.info("Making order for user id: {} with items: {}", userId, items.getCartItemIds().toString());

        List<CartItem> selectedCartItems = cartItemRepository.findAllById(items.getCartItemIds());

        List<OrderItem> orderItems = new ArrayList<>();

        Order order = new Order();
        Category category = categoryService.getCategoryEntityById(1L);

        order.setUser(userService.getUserById(userId));

        for (CartItem cartItem : selectedCartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice()); // get from product
            orderItem.setCategory(cartItem.getProduct().getCategory());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        BigDecimal totalPrice = orderItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(totalPrice);

        setOrderDetail(order);

        Order savedOrder = orderRepository.save(order);
        return getOrderResponseDTO(savedOrder);
    }

    // Thằng này để set order nhưng mà hard code
    private void setOrderDetail(Order order) {
        com.greenwich.ecommerce.common.enums.OrderStatus orderStatus = com.greenwich.ecommerce.common.enums.OrderStatus.PENDING;
        order.setOrderDate(LocalDateTime.now());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setDiscountApplied(BigDecimal.ZERO);
        order.setOrderStatus(1L); // or orderStatusService.getStatus("PENDING")
//        order.setOrderChannel() // Hardcode nhưng mà chưa implement
        order.setCsr(userService.getUserById(2L)); // hard code nen set thang so 2 la admin di

        Address address = addressRepository.getReferenceById(1L);
        order.setAddress(address);
        order.setTotal_amount(1);
    }

    private boolean isOrderBelongToUser(Order order, Long userId) {
        return orderItemRepository.existsByIdAndOrderUserId(order.getId(),userId);
    }

    private boolean isOrderItemBelongToUser(OrderItem orderItem, Long userId) {
        return orderItemRepository.existsByIdAndOrderUserId(orderItem.getId(),userId);
    }

    // cái này để kiểm tra cartItem trước khi biến thành order Item, t code dơ nên dùng luôn cart repo
    private boolean isCartItemBelongToUser(Long cartItemId, Long userId) {
        return cartItemRepository.existsByIdAndCartUserId(cartItemId, userId);
    }

}
