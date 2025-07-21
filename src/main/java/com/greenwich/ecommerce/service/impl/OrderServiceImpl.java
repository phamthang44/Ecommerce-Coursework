package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.common.enums.OrderStatusType;
import com.greenwich.ecommerce.dto.request.OrderItemRequestDTO;
import com.greenwich.ecommerce.dto.request.OrderRequestDTO;
import com.greenwich.ecommerce.dto.response.OrderItemResponseDTO;
import com.greenwich.ecommerce.dto.response.OrderResponseDTO;
import com.greenwich.ecommerce.entity.*;
import com.greenwich.ecommerce.exception.InvalidDataException;
import com.greenwich.ecommerce.exception.NotFoundException;
import com.greenwich.ecommerce.exception.UnauthorizedException;
import com.greenwich.ecommerce.repository.CartRepository;
import com.greenwich.ecommerce.repository.OrderChannelRepository;
import com.greenwich.ecommerce.repository.OrderRepository;
import com.greenwich.ecommerce.repository.OrderStatusRepository;
import com.greenwich.ecommerce.service.*;
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
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;
    private final CategoryService categoryService;
    private final AssetService assetService;
    private final UserService userService;
    private final CartService cartService;
    private final OrderChannelRepository orderChannelRepository;
    private final ProductService productService;
    private final OrderStatusRepository orderStatusRepository;
//
//    // lay tat ca order cua 1 thang user
//    public OrderResponseDTO getOrderByUserId(Long userId) {
//        log.info("Fetching order with user ID: {}", userId);
//        return null;
//    }
//
//    public OrderResponseDTO getOrderById(Long orderId) {
//        log.info("Fetching order with ID: {}", orderId);
//        /* cần validate */
//        Order order = orderRepository.getOrderById(orderId);
//
//        return getOrderResponseDTO(order);
//    }
//
//    private OrderResponseDTO getOrderResponseDTO(Order order) {
//        OrderResponseDTO orderResponse = new OrderResponseDTO(); // nay khi nao dung moi khai bao
//        orderResponse.setOrderItems(order.getOrderItems().stream()
//                .map(orderItem ->OrderItemResponseDTO.builder()
//                        .orderId(order.getId())
//                        .id(orderItem.getId())
//                        .productId(orderItem.getProduct().getId())
//                        .name(orderItem.getProduct().getName())
//                        .quantity(orderItem.getQuantity())
//                        .price(orderItem.getProduct().getPrice())
//                        .subTotalPrice(orderItem.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))) // neu minh phai tu tinh chu kh nho front end gui ve
//                        .assetUrl(getOrderItemAssetUrl(orderItem))
//                        .build())
//        .toList());
//
//        return orderResponse;
//    }
//
//    private String getOrderItemAssetUrl(OrderItem orderitem) {
//        if (orderitem.getProduct() != null && orderitem.getProduct().getAssets() != null && !orderitem.getProduct().getAssets().isEmpty()) {
//
//            Asset asset = assetService.getAssetByUsageId(orderitem.getProduct().getId());
//
//            return asset.getUrl();
//        }
//        return null;
//    }
//
//    public OrderResponseDTO makeOrder(Long userId) {
//        log.info("Making order for user id: {}", userId);
//
//        Cart cart = cartRepository.getByUserId(userId);
//        if (cart == null) {
//            log.error("Cart not found for user id: {}", userId);
//            throw new RuntimeException("Cart not found for user id: " + userId);
//        }
//
//        Order order = new Order();
//        order.setUser(userService.getUserById(userId));
//        order.setOrderItems(new ArrayList<>());
//
//
//        Category category = categoryService.getCategoryEntityById(1L);
//
//        // Vòng lặp lấy các cart item / cần 1 cái vòng lặp hoặc nhận 1 list các item khác để làm logic chỉ nhận 1 vài item thôi, khi nào thảo luận xong làm tiếp
//        // chọn lọc theo id nhận
//        for (CartItem cartItem : cart.getCartItems()) {
//            OrderItem orderItem = new OrderItem();
//            log.info("Adding product with id {} to cart for user {}", cartItem.getId(), userId);
//            orderItem.setProduct(cartItem.getProduct());
//            orderItem.setQuantity(cartItem.getQuantity());
//            orderItem.setCategory(category);
//            orderItem.setPrice(cartItem.getProduct().getPrice());
//            orderItem.setOrder(order);
//            order.getOrderItems().add(orderItem);
//        }
//
//        BigDecimal totalPrice = cart.getCartItems().stream()
//                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        order.setTotalPrice(totalPrice);
//        com.greenwich.ecommerce.common.enums.OrderStatusType orderStatus = com.greenwich.ecommerce.common.enums.OrderStatusType.PENDING;
//        order.setOrderDate(LocalDateTime.now());
//        order.setCreatedAt(LocalDateTime.now());
//        order.setUpdatedAt(LocalDateTime.now());
//        order.setDiscountApplied(BigDecimal.ZERO);
//        order.setOrderStatus(1L); // or orderStatusService.getStatus("PENDING")
////        order.setOrderChannel() // Hardcode nhưng mà chưa implement
//
//        return getOrderResponseDTO(orderRepository.save(order));
//    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO, Long userId) {
        if (orderRequestDTO == null || orderRequestDTO.getItems() == null || orderRequestDTO.getItems().isEmpty()) {
            log.error("Order request or items cannot be null or empty");
            throw new InvalidDataException("Order request or items cannot be null or empty");
        }

        log.info("Creating order for user ID: {}", userId);
        User user = userService.getUserById(userId);
        if (user == null) {
            log.error("User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }

        Order order = new Order();
        OrderChannel orderChannel = orderChannelRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Order channel not found with ID: 1"));
        Address address = user.getAddresses().stream().filter(
                addr -> addr.getId().equals(orderRequestDTO.getAddressId())
                )
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Default address not found for user ID: " + userId));



        BigDecimal totalPrice = orderRequestDTO.getItems().stream()
                .map(item -> {
                    Product product = productService.getProductEntityById(item.getProductId());
                    if (product == null) {
                        throw new NotFoundException("Product not found with ID: " + item.getProductId());
                    }
                    return product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        double discountPercent = orderRequestDTO.getDiscountPercent() != null ? orderRequestDTO.getDiscountPercent() : 15.0; // Default discount percent if not provided

        BigDecimal discountAmount = totalPrice.multiply(BigDecimal.valueOf(discountPercent));
        if (discountAmount.compareTo(totalPrice) > 0) {
            log.error("Discount amount cannot be greater than total price");
            throw new InvalidDataException("Discount amount cannot be greater than total price");
        }
        BigDecimal totalAmount = totalPrice.subtract(discountAmount);
        order.setUser(user);

        order.setOrderItems(orderRequestDTO.getItems().stream()
                .map(item -> {
                    OrderItem orderItem = new OrderItem();
                    Product product = productService.getProductEntityById(item.getProductId());
                    orderItem.setProduct(product);
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setPrice(orderItem.getProduct().getPrice());
                    orderItem.setCategory(product.getCategory());
                    orderItem.setSubtotal(orderItem.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                    orderItem.setOrder(order);
                    return orderItem;
                }).toList());
        order.setOrderStatus(orderStatusRepository.findByOrderStatusName(OrderStatusType.PENDING));
        order.setTotalPrice(totalPrice);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(totalAmount);
        order.setDiscountApplied(discountAmount);
        order.setOrderChannel(orderChannel);
        order.setAddress(address);
        order.setCsr(userService.getUserById(2L)); //hardcoded CSR ID, should be replaced with actual logic to get CSR

        return getOrderResponseDTO(orderRepository.save(order), userId);
    }

    private OrderResponseDTO getOrderResponseDTO(Order order) {
        if (order == null) {
            log.error("Order not found");
            throw new NotFoundException("Order not found");
        }

        List<OrderItemResponseDTO> orderItems = order.getOrderItems().stream()
                .map(item -> OrderItemResponseDTO.builder()
                        .orderItemId(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .subTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .assetUrl(assetService.getAssetByUsageId(item.getProduct().getId()).getUrl())
                        .build())
                .toList();

        return OrderResponseDTO.builder()
                .orderId(order.getId())
                .orderItems(orderItems)
                .totalPrice(order.getTotalPrice())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .discountApplied(order.getDiscountApplied())
                .status(String.valueOf(order.getOrderStatus().getStatusName()))
                .addressLine(order.getAddress().getUserAddress())
                .customerName(order.getUser().getFullName())
                .build();
    }

    private OrderResponseDTO getOrderResponseDTO(Order order, Long userId) {
        if (order == null || !order.getUser().getId().equals(userId)) {
            log.error("Order not found or does not belong to user ID: {}", userId);
            throw new UnauthorizedException("Order not found or does not belong to user ID: " + userId);
        }
        return getOrderResponseDTO(order);
    }

    @Override
    public OrderResponseDTO getOrderById(Long orderId) {
        return null;
    }

    @Override
    public OrderResponseDTO createOrderWithAllItems(Long userId) {
        return null;
    }

    @Override
    public OrderResponseDTO createOrderWithSelectedItems(OrderItemRequestDTO items, Long userId) {
        return null;
    }
}
