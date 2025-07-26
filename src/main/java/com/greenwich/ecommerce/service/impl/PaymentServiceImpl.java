package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.common.enums.OrderStatusType;
import com.greenwich.ecommerce.common.enums.PaymentStatusType;
import com.greenwich.ecommerce.common.util.Util;
import com.greenwich.ecommerce.dto.request.CreatePaymentInput;
import com.greenwich.ecommerce.dto.request.PaymentProcessRequest;
import com.greenwich.ecommerce.dto.response.*;
import com.greenwich.ecommerce.entity.*;
import com.greenwich.ecommerce.exception.*;
import com.greenwich.ecommerce.infra.email.EmailService;
import com.greenwich.ecommerce.repository.OrderRepository;
import com.greenwich.ecommerce.repository.OrderStatusRepository;
import com.greenwich.ecommerce.repository.PaymentRepository;
import com.greenwich.ecommerce.repository.PaymentStatusRepository;
import com.greenwich.ecommerce.service.PaymentService;
import com.greenwich.ecommerce.service.ProductService;
import com.greenwich.ecommerce.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final PaymentValidator paymentValidator;
    private final OrderStatusRepository orderStatusRepository;
    private final EmailService emailService;
    private final ProductService productService;

    @Override
    @Transactional
    public PaymentCreateResponse createPayment(CreatePaymentInput input, Long userId) {
        paymentValidator.validatePaymentInputRequest(input, userId);

        log.info("Creating payment for order ID: {}, amount: {}", input.getOrderId(), input.getTotalAmount());
        String visaCheckReference = generateVisaCheckReference(input.getOrderId(), input.getTotalAmount());
        log.info("Creating payment with Visa Check Reference: {}", visaCheckReference);
        PaymentStatus paymentStatus = paymentStatusRepository.findByPaymentStatus(PaymentStatusType.PENDING);
        User user = userService.getUserById(userId);
        Order order = orderRepository.findById(input.getOrderId())
                .orElseThrow(() -> new InvalidDataException("Order not found with ID: " + input.getOrderId()));

        if (!Objects.equals(order.getUser().getId(), user.getId())) {
            throw new UnauthorizedException("Unauthorized user for payment of order ID: " + input.getOrderId());
        }

        if (!Objects.equals(input.getTotalAmount(), order.getTotalAmount())) {
            throw new BadRequestException("Payment amount does not match order total amount.");
        }

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setOrder(order);
        payment.setAmount(input.getTotalAmount());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(paymentStatus);
        payment.setVisaCheckReference(visaCheckReference);

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment created successfully with ID: {}", savedPayment.getId());
        return createPaymentResponse(savedPayment);
    }

    private PaymentCreateResponse createPaymentResponse(Payment payment) {
        return PaymentCreateResponse.builder()
                .visaCheckRef(payment.getVisaCheckReference())
                .paymentTime(payment.getPaymentDate())
                .status(String.valueOf(payment.getStatus().getStatusName()))
                .build();
    }



    private String generateVisaCheckReference(Long orderId, BigDecimal amount) {
        if (orderId != null && amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            String reference = "VISA-" + orderId + "-" + amount.toPlainString() + "-" + UUID.randomUUID();
            log.info("Generated Visa Check Reference: {}", reference);
            return reference;
        }
        throw new InvalidDataException("OrderId and amount must be valid to generate visa reference.");
    }

    @Override
    public boolean verifyPayment(String visaCheckReference) {
        return Math.random() < 0.9;
    }

    @Override
    @Transactional
    public boolean validateAndUpdatePaymentStatus(String visaCheckReference) {
        Payment payment = paymentRepository.findByVisaCheckReference(visaCheckReference)
                .orElseThrow(() -> new PaymentException("Payment not found"));
        if (!payment.getStatus().equals(paymentStatusRepository.findByPaymentStatus(PaymentStatusType.PENDING))) {
            throw new PaymentException("The payment must be followed by right flow!");
        }
        boolean isSuccess = verifyPayment(visaCheckReference); // Gọi hàm verify

        PaymentStatusType statusType = isSuccess ? PaymentStatusType.SUCCESS : PaymentStatusType.FAILED;
        PaymentStatus status = paymentStatusRepository.findByPaymentStatus(statusType);
        payment.setStatus(status);
        paymentRepository.save(payment);

        if (isSuccess) {
            Order order = payment.getOrder();
            OrderStatus orderStatus = orderStatusRepository.findByOrderStatusName(OrderStatusType.PAID);
            order.setOrderStatus(orderStatus);
            orderRepository.save(order);

            log.info("Update order status: {}", orderStatus.getStatusName());

            // Update product stock if payment is successful
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem item : orderItems) {
                Product product = item.getProduct();
                productService.decreaseProductQuantity(product.getId(), item.getQuantity());
            }
        }

        return isSuccess;
    }

    @Override
    @Transactional
    public PaymentProcessResponse processPayment(PaymentProcessRequest request, Long userId) {
        boolean isSuccess = validateAndUpdatePaymentStatus(request.getVisaCheckReference());
        if (!isSuccess) {
            throw new RuntimeException("Payment process failed for reference: " + request.getVisaCheckReference());
        }
        Payment payment = paymentRepository.findByVisaCheckReference(request.getVisaCheckReference())
                .orElseThrow(() -> new RuntimeException("Payment not found for reference: " + request.getVisaCheckReference()));
        User user = userService.getUserById(userId);
        if (!Objects.equals(payment.getUser().getId(), user.getId())) {
            throw new UnauthorizedException("Unauthorized user for payment reference: " + request.getVisaCheckReference());
        }
        log.info("Payment processed successfully for reference: {}", request.getVisaCheckReference());

        String message = "Payment verified and processed successfully! Your order is paid successfully.";
        try {
            emailService.sendReceiptEmail(payment);
        } catch (Exception e) {
            log.error("Failed to send payment confirmation email: {}", e.getMessage());
        }
        return PaymentProcessResponse.builder()
                .visaCheckReference(payment.getVisaCheckReference())
                .paymentTime(payment.getPaymentDate())
                .status(String.valueOf(payment.getStatus().getStatusName()))
                .message(message)
                .build();
    }

    @Override
    public PaymentStatus getPaymentStatusByReference(String visaCheckReference) {
        return null;
    }

    @Override
    public PaymentStatus getPaymentStatusById(Long id) {
        return null;
    }

    @Override
    public Payment getPaymentByReference(String visaCheckReference) {
        return paymentRepository.findByVisaCheckReference(visaCheckReference)
                .orElseThrow(() -> new NotFoundException("Payment not found with Visa Check Reference: " + visaCheckReference));
    }

    @Override
    public Payment getPaymentById(Long id) {

        return paymentRepository.findById(id)
                .orElseThrow(() -> new InvalidDataException("Payment not found with ID: " + id));

    }

    private PaymentResponse convertToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .paymentDate(payment.getPaymentDate())
                .amount(payment.getAmount())
                .orderCode(payment.getOrder().getId().toString())
                .status(payment.getStatus().getStatusName().toString())
                .build();
    }

    @Override
    public PageResponse<PaymentResponse> getPaymentsWithPage(int pageNo, int pageSize, Long userId) {
        int page = Util.getPageNo(pageNo); // Convert to zero-based index

        if (pageNo < 0 || pageSize <= 0) {
            log.error("Invalid pagination parameters: pageNo={}, pageSize={}", pageNo, pageSize);
            throw new InvalidDataException("Page number and size must be non-negative and positive respectively");
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Payment> payments = paymentRepository.findByUserId(userId, pageable);
        List<PaymentResponse> paymentResponses = payments.stream().map(this::convertToPaymentResponse).toList();

        return PageResponse.<PaymentResponse>builder()
                .pageNo(page)
                .pageSize(pageSize)
                .totalPages(payments.getTotalPages())
                .totalElements((int) payments.getTotalElements())
                .items(paymentResponses)
                .build();
    }


}

