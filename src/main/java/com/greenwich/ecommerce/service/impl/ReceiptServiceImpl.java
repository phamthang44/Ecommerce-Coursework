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
    private final ReceiptServiceValidator receiptServiceValidator;
    private final ReceiptRepository receiptRepository;
    private final UserService userService;
    private final PaymentService paymentService;

    @Override
    public ReceiptResponseDTO getReceiptById(Long receiptId, Long userId) {
        receiptServiceValidator.validateUserId(userId);
        receiptServiceValidator.validateReceiptId(receiptId);

        if(receiptId == null) {
            log.error("Receipt ID cannot be null");
            throw new IllegalArgumentException("Receipt ID cannot be null");
        }

        if( receiptRepository.findById(receiptId).isEmpty()) {
            log.error("Receipt not found");
            throw new IllegalArgumentException("Receipt not found");
        }

        Receipt receipt = receiptRepository.getReceiptById(receiptId);

        if (!isReceiptBelongToUser(userId, receipt)) {
            log.error("Receipt does not belong to the user");
            throw new IllegalArgumentException("Receipt does not belong to the user");
        }

        return getReceiptResponseDTO(receipt);
    }

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
                .paymentStatus(receipt.getPayment().getStatus().getStatusName().toString())
                .build();
    }

    @Override
    public ReceiptResponseDTO createReceipt(ReceiptRequestDTO receiptRequestDTO, Long userId) {

        receiptServiceValidator.validateReceiptRequest(receiptRequestDTO);

        User user = userService.getUserById(userId);

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

    private boolean isReceiptBelongToUser(Long userId, Receipt receipt) {
        return receipt.getUser().getId().equals(userId);
    }
}
