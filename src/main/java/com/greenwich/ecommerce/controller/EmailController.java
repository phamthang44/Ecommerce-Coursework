package com.greenwich.ecommerce.controller;

import com.greenwich.ecommerce.dto.response.EmailConfirmResponse;
import com.greenwich.ecommerce.entity.Order;
import com.greenwich.ecommerce.infra.email.EmailService;
import com.greenwich.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
//    private final OrderService orderService;

    @GetMapping("/confirm")
    public ResponseEntity<EmailConfirmResponse> handleConfirm(@RequestParam("token") String token) {

        EmailConfirmResponse responseData = emailService.confirmToken(token);

        return ResponseEntity.status(200).body(responseData);
    }

    @GetMapping("/receipt")
    public ResponseEntity<String> handleReceipt() {
//        Order order = orderService.();

//        emailService.sendReceiptEmail();
        return ResponseEntity.ok("Receipt sent successfully to ");
    }
}
