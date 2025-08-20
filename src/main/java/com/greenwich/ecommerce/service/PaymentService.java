package com.greenwich.ecommerce.service;

import com.greenwich.ecommerce.dto.request.CreatePaymentInput;
import com.greenwich.ecommerce.dto.request.PaymentProcessRequest;
import com.greenwich.ecommerce.dto.response.PageResponse;
import com.greenwich.ecommerce.dto.response.PaymentCreateResponse;
import com.greenwich.ecommerce.dto.response.PaymentProcessResponse;
import com.greenwich.ecommerce.dto.response.PaymentResponse;
import com.greenwich.ecommerce.entity.Payment;
import com.greenwich.ecommerce.entity.PaymentStatus;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    boolean verifyPayment(String visaCheckReference);
    boolean validateAndUpdatePaymentStatus(String visaCheckReference);

    PaymentStatus getPaymentStatusByReference(String visaCheckReference);
    PaymentStatus getPaymentStatusById(Long id);
    Payment getPaymentByReference(String visaCheckReference);
    Payment getPaymentById(Long id);

    PaymentCreateResponse createPayment(CreatePaymentInput input, Long userId);
    PaymentProcessResponse processPayment(PaymentProcessRequest request, Long userId);

    PageResponse<PaymentResponse> getPaymentsWithPage(int pageNo, int pageSize, Long userId);

    Payment getPaymentByOrderCode(String orderCode);

}
