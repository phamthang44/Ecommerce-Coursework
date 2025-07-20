package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.dto.request.OrderItemRequestDTO;
import com.greenwich.ecommerce.dto.response.OrderItemResponseDTO;
import com.greenwich.ecommerce.dto.response.OrderResponseDTO;
import com.greenwich.ecommerce.entity.*;
import com.greenwich.ecommerce.repository.CartRepository;
import com.greenwich.ecommerce.repository.OrderRepository;
import com.greenwich.ecommerce.service.CartService;
import com.greenwich.ecommerce.service.CategoryService;
import com.greenwich.ecommerce.service.ProductService;
import com.greenwich.ecommerce.service.UserService;
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
    private final CartRepository cartRepository;
    private final ProductService productService;

    // lay tat ca order cua 1 thang user
    public OrderResponseDTO getOrderByUserId(Long userId) {
        log.info("Fetching order with user ID: {}", userId);
        return null;
    }

    public OrderResponseDTO getOrderById(Long orderId) {
        log.info("Fetching order with ID: {}", orderId);
        /* cần validate */
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
                        .subTotalPrice(orderItem.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))) // neu minh phai tu tinh chu kh nho front end gui ve
                        .assetUrl(getOrderItemAssetUrl(orderItem))
                        .build())
        .toList());

        return orderResponse;
    }

    private String getOrderItemAssetUrl(OrderItem orderitem) {
        if (orderitem.getProduct() != null && orderitem.getProduct().getAssets() != null && !orderitem.getProduct().getAssets().isEmpty()) {

            Asset asset = assetService.getAssetByUsageId(orderitem.getProduct().getId());

            return asset.getUrl();
        }
        return null;
    }

    public OrderResponseDTO makeOrder(Long userId) {
        log.info("Making order for user id: {}", userId);

        Cart cart = cartRepository.getByUserId(userId);
        if (cart == null) {
            log.error("Cart not found for user id: {}", userId);
            throw new RuntimeException("Cart not found for user id: " + userId);
        }

        Order order = new Order();
        order.setUser(userService.getUserById(userId));
        order.setOrderItems(new ArrayList<>());


        Category category = categoryService.getCategoryEntityById(1L);

        // Vòng lặp lấy các cart item / cần 1 cái vòng lặp hoặc nhận 1 list các item khác để làm logic chỉ nhận 1 vài item thôi, khi nào thảo luận xong làm tiếp
        // chọn lọc theo id nhận
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
        com.greenwich.ecommerce.common.enums.OrderStatusType orderStatus = com.greenwich.ecommerce.common.enums.OrderStatusType.PENDING;
        order.setOrderDate(LocalDateTime.now());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setDiscountApplied(BigDecimal.ZERO);
        order.setOrderStatus(1L); // or orderStatusService.getStatus("PENDING")
//        order.setOrderChannel() // Hardcode nhưng mà chưa implement

        return getOrderResponseDTO(orderRepository.save(order));
    }
}
