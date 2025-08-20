package com.greenwich.ecommerce.service;

import com.greenwich.ecommerce.dto.request.ReceiptRequestDTO;
import com.greenwich.ecommerce.dto.response.PageResponse;
import com.greenwich.ecommerce.dto.response.ReceiptResponseDTO;


public interface ReceiptService {
    ReceiptResponseDTO getReceiptById(Long receiptId, Long userId);
    ReceiptResponseDTO createReceipt(ReceiptRequestDTO receiptRequestDTO, Long  userId);

    PageResponse<ReceiptResponseDTO> getAllReceiptsByUserId(Long userId, int page, int size);

    ReceiptResponseDTO getReceiptByOrderCode(String orderCode);

}
