package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.dto.request.OrderRequestDTO;
import com.greenwich.ecommerce.dto.request.ReceiptRequestDTO;
import com.greenwich.ecommerce.dto.response.OrderResponseDTO;
import com.greenwich.ecommerce.dto.response.ReceiptResponseDTO;
import com.greenwich.ecommerce.entity.Payment;
import com.greenwich.ecommerce.entity.Receipt;
import com.greenwich.ecommerce.entity.User;
import com.greenwich.ecommerce.repository.ReceiptRepository;
import com.greenwich.ecommerce.service.PaymentService;
import com.greenwich.ecommerce.service.ReceiptService;
import com.greenwich.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReceiptServiceImpl implements ReceiptService {
    private final OrderServiceValidator orderServiceValidator;
    private final ReceiptRepository receiptRepository;
    private final UserService userService;
    private final PaymentService paymentService;

//    @Override
//    public ReceiptResponseDTO getReceiptById(ReceiptResponseDTO receiptRequest) {
//        receiptRepository.findById(receiptRequest.getId());
//
//
//
//
//        return null;
//    }

    private ReceiptResponseDTO getReceiptResponseDTO(Receipt receipt) {
        if (receipt == null) {
            log.error("Receipt not found");
            throw new IllegalArgumentException("Receipt not found");
        }

        return ReceiptResponseDTO.builder()
                .id(receipt.getId())
                .paymentId(receipt.getPayment().getId())
                .orderId(receipt.getPayment().getOrder().getId())
                .finalPrice(receipt.getPayment().getAmount())
                .paymentStatus(receipt.getPayment().getStatus().toString())
                .referenceNumber(receipt.getPayment().getVisaCheckReference())
                .build();
    }

    public ReceiptResponseDTO createReceipt(ReceiptRequestDTO receiptRequestDTO, User user) {
        if (receiptRequestDTO == null) {
            log.error("Receipt request is null");
            throw new IllegalArgumentException("Receipt request is null");
        }

        if (user == null) {
            log.error("User not found");
            throw new IllegalArgumentException("User not found");
        }

        Receipt receipt = new Receipt();
        Payment payment = paymentService.getPaymentById(receiptRequestDTO.getPaymentId());

        receipt.setPayment(payment);
        receipt.setUser(user);
        receipt.setCreatedAt(payment.getCreatedAt());
        receipt.setUpdatedAt(payment.getUpdatedAt());

        receiptRepository.save(receipt);

        return getReceiptResponseDTO(receipt);
    }
}
