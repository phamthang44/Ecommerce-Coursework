package com.greenwich.ecommerce.service;

import com.greenwich.ecommerce.dto.request.ReceiptRequestDTO;
import com.greenwich.ecommerce.dto.response.ReceiptResponseDTO;
import com.greenwich.ecommerce.entity.User;

public interface ReceiptService {
    ReceiptResponseDTO createReceipt(ReceiptRequestDTO receiptRequestDTO, User user);
}
