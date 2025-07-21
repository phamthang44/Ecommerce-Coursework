package com.greenwich.ecommerce.controller;

import com.greenwich.ecommerce.dto.response.EmailConfirmResponse;
import com.greenwich.ecommerce.infra.email.EmailService;
import com.greenwich.ecommerce.service.OrderService;
import com.greenwich.ecommerce.service.PaymentService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final OrderService orderService;
    private final PaymentService paymentService;

    @GetMapping("/confirm")
    public ResponseEntity<EmailConfirmResponse> handleConfirm(@RequestParam("token") String token) {

        EmailConfirmResponse responseData = emailService.confirmToken(token);

        return ResponseEntity.status(200).body(responseData);
    }

    @GetMapping("/receipt/{paymentId}")
    public ResponseEntity<String> handleReceipt(@PathVariable Long paymentId) throws MessagingException {
        emailService.sendReceiptEmail(paymentService.getPaymentById(paymentId));
        return ResponseEntity.accepted().body("Receipt sent successfully");
    }
}
