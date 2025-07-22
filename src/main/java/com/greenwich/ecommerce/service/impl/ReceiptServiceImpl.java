package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.common.util.Util;
import com.greenwich.ecommerce.dto.request.OrderRequestDTO;
import com.greenwich.ecommerce.dto.request.ReceiptRequestDTO;
import com.greenwich.ecommerce.dto.response.OrderResponseDTO;
import com.greenwich.ecommerce.dto.response.PageResponse;
import com.greenwich.ecommerce.dto.response.ReceiptResponseDTO;
import com.greenwich.ecommerce.entity.Payment;
import com.greenwich.ecommerce.entity.Receipt;
import com.greenwich.ecommerce.entity.User;
import com.greenwich.ecommerce.exception.InvalidDataException;
import com.greenwich.ecommerce.exception.NotFoundException;
import com.greenwich.ecommerce.exception.UnauthorizedException;
import com.greenwich.ecommerce.repository.ReceiptRepository;
import com.greenwich.ecommerce.service.PaymentService;
import com.greenwich.ecommerce.service.ReceiptService;
import com.greenwich.ecommerce.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
            throw new InvalidDataException("Receipt ID cannot be null");
        }

        if( receiptRepository.findById(receiptId).isEmpty()) {
            log.error("Get receipt : Receipt not found");
            throw new NotFoundException("Receipt not found");
        }

        Receipt receipt = receiptRepository.getReceiptById(receiptId);

        if (!isReceiptBelongToUser(userId, receipt)) {
            log.error("Receipt does not belong to the user");
            throw new UnauthorizedException("Receipt does not belong to the user");
        }

        return getReceiptResponseDTO(receipt);
    }

    private ReceiptResponseDTO getReceiptResponseDTO(Receipt receipt) {
        if (receipt == null) {
            throw new NotFoundException("Receipt not found");
        }

        return ReceiptResponseDTO.builder()
                .id(receipt.getId())
                .paymentStatus(receipt.getPayment().getStatus().getStatusName().toString())
                .finalPrice(receipt.getPayment().getAmount())
                .visaCheckRef(receipt.getPayment().getVisaCheckReference())
                .paymentDate(receipt.getPayment().getPaymentDate())
                .build();
    }

    @Override
    @Transactional
    public ReceiptResponseDTO createReceipt(ReceiptRequestDTO receiptRequestDTO, Long userId) {

        receiptServiceValidator.validateReceiptRequest(receiptRequestDTO, userId);

        User user = userService.getUserById(userId);

        if (user == null) {
            log.error("User not found");
            throw new IllegalArgumentException("User not found");
        }

        Receipt receipt = new Receipt();
        Payment payment = paymentService.getPaymentByReference(receiptRequestDTO.getVisaCheckRef());
        if (!Objects.equals(payment.getUser().getId(), user.getId())) {
            log.error("Payment does not belong to the user");
            throw new UnauthorizedException("Payment does not belong to the user");
        }
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

    @Override
    public PageResponse<ReceiptResponseDTO> getAllReceiptsByUserId(Long userId, int pageNo, int pageSize) {
        int page = Util.getPageNo(pageNo);
        if (pageSize <= 0) {
            log.error("Page size must be greater than zero");
            throw new InvalidDataException("Page size must be greater than zero");
        }

        receiptServiceValidator.validateUserId(userId);
        User user = userService.getUserById(userId);

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Receipt> receipts = receiptRepository.findAllByDeletedFalseAndUserId(pageable, user.getId());
        List<ReceiptResponseDTO> receiptResponseDTOs = receipts.stream()
                .map(this::getReceiptResponseDTO)
                .toList();

        return PageResponse.<ReceiptResponseDTO>builder()
                .pageNo(page)
                .pageSize(pageSize)
                .totalPages(receipts.getTotalPages())
                .totalElements((int) receipts.getTotalElements())
                .items(receiptResponseDTOs)
                .build();
    }
}
