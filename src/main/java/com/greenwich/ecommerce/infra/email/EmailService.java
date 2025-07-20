package com.greenwich.ecommerce.infra.email;

import com.greenwich.ecommerce.dto.response.EmailConfirmResponse;
import com.greenwich.ecommerce.entity.OrderItem;
import com.greenwich.ecommerce.entity.Payment;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendRegistrationEmail(String toEmail,
                                String subject,
                                String body) {


        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("${spring.mail.username}");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendEmailRegistrationHtml(String toEmail, String username) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("Welcome to CheapDeals.com LTD! Please Confirm Your Email.");

        String htmlContentTest = """
                <!DOCTYPE html>
                        <html lang="en">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Welcome to CheapDeals.com</title>
                            <style>
                                * {
                                    margin: 0;
                                    padding: 0;
                                    box-sizing: border-box;
                                }
               \s
                                body {
                                    font-family: 'Arial', sans-serif;
                                    line-height: 1.6;
                                    color: #333;
                                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                                    min-height: 100vh;
                                    padding: 20px;
                                }
               \s
                                .email-container {
                                    max-width: 600px;
                                    margin: 0 auto;
                                    background: #ffffff;
                                    border-radius: 15px;
                                    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
                                    overflow: hidden;
                                    animation: slideIn 0.8s ease-out;
                                }
               \s
                                @keyframes slideIn {
                                    from {
                                        opacity: 0;
                                        transform: translateY(30px);
                                    }
                                    to {
                                        opacity: 1;
                                        transform: translateY(0);
                                    }
                                }
               \s
                                .header {
                                    background: linear-gradient(135deg, #07f7b6 0%, #00c3c1 50%, #007a78 100%);
                                    color: white;
                                    padding: 40px 30px;
                                    text-align: center;
                                    position: relative;
                                    overflow: hidden;
                                }
               \s
                                .header::before {
                                    content: '';
                                    position: absolute;
                                    top: -50%;
                                    left: -50%;
                                    width: 200%;
                                    height: 200%;
                                    background: repeating-linear-gradient(
                                        45deg,
                                        transparent,
                                        transparent 10px,
                                        rgba(255, 255, 255, 0.05) 10px,
                                        rgba(255, 255, 255, 0.05) 20px
                                    );
                                    animation: float 20s linear infinite;
                                }
               \s
                                @keyframes float {
                                    0% { transform: translate(-50%, -50%) rotate(0deg); }
                                    100% { transform: translate(-50%, -50%) rotate(360deg); }
                                }
               \s
                                .header h1 {
                                    font-size: 2.5em;
                                    margin-bottom: 10px;
                                    position: relative;
                                    z-index: 1;
                                    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
                                }
               \s
                                .header .tagline {
                                    font-size: 1.1em;
                                    opacity: 0.9;
                                    position: relative;
                                    z-index: 1;
                                }
               \s
                                .content {
                                    padding: 40px 30px;
                                }
               \s
                                .welcome-message {
                                    text-align: center;
                                    margin-bottom: 30px;
                                }
               \s
                                .welcome-message h2 {
                                    color: #2c3e50;
                                    font-size: 2em;
                                    margin-bottom: 15px;
                                    background: linear-gradient(135deg, #667eea, #764ba2);
                                    -webkit-background-clip: text;
                                    -webkit-text-fill-color: transparent;
                                    background-clip: text;
                                }
                       \s
                                .welcome-message p {
                                    color: #666;
                                    font-size: 1.1em;
                                    line-height: 1.8;
                                }
                       \s
                                .features {
                                    background: #f8f9fa;
                                    padding: 30px;
                                    border-radius: 10px;
                                    margin: 30px 0;
                                    border-left: 5px solid #ff6b6b;
                                }
                       \s
                                .features h3 {
                                    color: #2c3e50;
                                    margin-bottom: 20px;
                                    font-size: 1.3em;
                                }
                       \s
                                .feature-list {
                                    color: #555;
                                    line-height: 1.8;
                                }
                       \s
                                .feature-item {
                                    margin: 10px 0;
                                    padding-left: 20px;
                                    position: relative;
                                }
                       \s
                                .feature-item::before {
                                    content: '✓';
                                    position: absolute;
                                    left: 0;
                                    color: #27ae60;
                                    font-weight: bold;
                                    font-size: 1.2em;
                                }
                       \s
                                .cta-section {
                                    text-align: center;
                                    margin: 40px 0;
                                }
                       \s
                                .cta-button {
                                    display: inline-block;
                                    background: linear-gradient(135deg,#07f7b6 0%,#00c3c1 50%,#007a78 100%);
                                    color: white;
                                    text-decoration: none;
                                    padding: 15px 40px;
                                    border-radius: 50px;
                                    font-size: 1.1em;
                                    font-weight: bold;
                                    transition: all 0.3s ease;
                                    box-shadow: 0 8px 15px rgba(255, 107, 107, 0.3);
                                    position: relative;
                                    overflow: hidden;
                                }
                       \s
                                .cta-button::before {
                                    content: '';
                                    position: absolute;
                                    top: 0;
                                    left: -100%;
                                    width: 100%;
                                    height: 100%;
                                    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
                                    transition: left 0.5s;
                                }
                       \s
                                .cta-button:hover::before {
                                    left: 100%;
                                }
                       \s
                                .cta-button:hover {
                                    transform: translateY(-2px);
                                    box-shadow: 0 12px 25px rgba(255, 107, 107, 0.4);
                                }
                       \s
                                .footer {
                                    background: #2c3e50;
                                    color: white;
                                    padding: 30px;
                                    text-align: center;
                                }
                       \s
                                .footer p {
                                    margin: 10px 0;
                                    opacity: 0.8;
                                }
                       \s
                                .social-links {
                                    margin: 20px 0;
                                }
                       \s
                                .social-links a {
                                    display: inline-block;
                                    width: 40px;
                                    height: 40px;
                                    background: #34495e;
                                    color: white;
                                    text-decoration: none;
                                    border-radius: 50%;
                                    margin: 0 10px;
                                    line-height: 40px;
                                    transition: all 0.3s ease;
                                }
                       \s
                                .social-links a:hover {
                                    background: #ff6b6b;
                                    transform: scale(1.1);
                                }
                       \s
                                @media (max-width: 600px) {
                                    .email-container {
                                        margin: 10px;
                                        border-radius: 10px;
                                    }
                       \s
                                    .header {
                                        padding: 30px 20px;
                                    }
                       \s
                                    .header h1 {
                                        font-size: 2em;
                                    }
                       \s
                                    .content {
                                        padding: 30px 20px;
                                    }
                       \s
                                    .welcome-message h2 {
                                        font-size: 1.6em;
                                    }
                       \s
                                    .cta-button {
                                        padding: 12px 30px;
                                        font-size: 1em;
                                    }
                                }
                            </style>
                        </head>
                        <body>
                            <div class="email-container">
                                <div class="header">
                                    <h1>CheapDeals.com</h1>
                                    <p class="tagline">Your Gateway to Amazing Savings</p>
                                </div>
                       \s
                                <div class="content">
                                    <div class="welcome-message">
                                        <h2>Welcome, %s!</h2>
                                        <p>We're thrilled to have you join the CheapDeals.com family! Get ready to discover incredible deals, exclusive offers, and unbeatable savings on products you love.</p>
                                    </div>
                       \s
                                    <div class="features">
                                        <h3>What's waiting for you:</h3>
                                        <div class="feature-list">
                                            <div class="feature-item">Daily deals with up to 80% off retail prices</div>
                                            <div class="feature-item">Exclusive member-only flash sales</div>
                                            <div class="feature-item">Personalized recommendations just for you</div>
                                            <div class="feature-item">Early access to seasonal promotions</div>
                                            <div class="feature-item">15% off when purchasing via Mobile App.</div>
                                        </div>
                                    </div>
                       \s
                                    <div class="cta-section">
                                        <p style="margin-bottom: 20px; color: #666; font-size: 1.1em;">Ready to start saving?</p>
                                        <a href="http://localhost:3000/login" class="cta-button">
                                            Start Shopping Now
                                        </a>
                                    </div>
                                </div>
                       \s
                                <div class="footer">
                                    <div class="social-links">
                                        <a href="#" title="Facebook">f</a>
                                        <a href="#" title="Twitter">t</a>
                                        <a href="#" title="Instagram">i</a>
                                        <a href="#" title="YouTube">y</a>
                                    </div>
                                    <p><strong>CheapDeals.com LTD</strong></p>
                                    <p>Making quality affordable for everyone</p>
                                    <p style="font-size: 0.9em; margin-top: 15px;">
                                        This email was sent because you registered for an account with CheapDeals.com<br>
                                        If you have any questions, contact us at cheapdeal466@gmail.com
                                    </p>
                                </div>
                            </div>
                        </body>
                        </html>
               \s""";
        String safeHtml = htmlContentTest.replace("%", "%%").replace("%%s", "%s");
        String htmlContentFormatted = String.format(safeHtml, username);

        helper.setText(htmlContentFormatted, true);

        mailSender.send(message);
    }

    public EmailConfirmResponse confirmToken(String token) {
        if (token.equals("123456")) {
            return new EmailConfirmResponse("Token is valid!", token);
        }
        return null;
    }


    public void sendReceiptEmail(Payment payment) throws MessagingException {
        String orderId = "CD-2025-001234"; //hardcoded for demo
        String orderDate = "July 19, 2025"; //hardcoded for demo
        String estimatedDelivery = "July 23, 2025"; //hardcoded for demo
        String billingAddress = """
                John Doe </br>
                123 Main Street </br>
                District 1, Ho Chi Minh City </br>
                Vietnam 70000 </br>
                """;
        String shippingAddress = """
                John Doe </br>
                456 Nguyen Trai Street </br>
                District 5, Ho Chi Minh City </br>
                Vietnam 70000 </br>
                """;
        StringBuilder itemTable = getStringBuilder(payment);
        BigDecimal subTotal = payment.getOrder().getTotalPrice(); //note cho nay la total price chua giam gia
        BigDecimal discount = payment.getOrder().getDiscountApplied();
        BigDecimal totalPaid = payment.getAmount();

        String totalSection = generateTotalSection(
                String.format("$%.2f", subTotal),
                String.format("$%.2f", discount),
                String.format("$%.2f", totalPaid)
        );

        String htmlContent = """
                <!DOCTYPE html>
                <html lang="en">
                  <head>
                    <meta charset="UTF-8" />
                    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                    <title>Your Order Receipt - CheapDeals.com</title>
                    <style>
                      * {
                        margin: 0;
                        padding: 0;
                        box-sizing: border-box;
                      }
                
                      body {
                        font-family: Arial, sans-serif;
                        line-height: 1.5;
                        color: #333333;
                        background-color: #f5f5f5;
                        padding: 20px;
                      }
                
                      .email-container {
                        max-width: 650px;
                        margin: 0 auto;
                        background: #ffffff;
                        border-radius: 8px;
                        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                        overflow: hidden;
                      }
                
                      .header {
                        background: linear-gradient(135deg, #07f7b6, #00d2ff);
                        color: white;
                        padding: 30px;
                        text-align: center;
                      }
                
                      .header h1 {
                        font-size: 28px;
                        margin-bottom: 8px;
                        font-weight: bold;
                      }
                
                      .header .receipt-title {
                        font-size: 18px;
                        opacity: 0.9;
                      }
                
                      .receipt-header {
                        padding: 25px 30px;
                        background: #f8f9fa;
                        border-bottom: 2px solid #e9ecef;
                      }
                
                      .receipt-info {
                        display: table;
                        width: 100%;
                      }
                
                      .receipt-left,
                      .receipt-right {
                        display: table-cell;
                        vertical-align: top;
                        width: 50%;
                      }
                
                      .receipt-right {
                        text-align: right;
                      }
                
                      .order-number {
                        font-size: 20px;
                        font-weight: bold;
                        color: #2c3e50;
                        margin-bottom: 5px;
                      }
                
                      .order-date {
                        color: #666;
                        font-size: 14px;
                      }
                
                      .customer-info {
                        padding: 25px 30px;
                        background: #ffffff;
                      }
                
                      .info-section {
                        margin-bottom: 25px;
                      }
                
                      .info-title {
                        font-weight: bold;
                        color: #2c3e50;
                        margin-bottom: 8px;
                        font-size: 16px;
                      }
                
                      .info-content {
                        color: #555;
                        line-height: 1.6;
                      }
                
                      .items-table {
                        width: 100%;
                        border-collapse: collapse;
                        margin: 0;
                      }
                
                      .items-header {
                        background: #2c3e50;
                        color: white;
                      }
                
                      .items-header th {
                        padding: 15px 20px;
                        text-align: left;
                        font-weight: bold;
                        font-size: 14px;
                      }
                
                      .items-header th:last-child {
                        text-align: right;
                      }
                
                      .item-row {
                        border-bottom: 1px solid #e9ecef;
                      }
                
                      .item-row td {
                        padding: 15px 20px;
                        vertical-align: top;
                      }
                
                      .item-row:nth-child(even) {
                        background: #f8f9fa;
                      }
                
                      .item-name {
                        font-weight: bold;
                        color: #2c3e50;
                        margin-bottom: 4px;
                      }
                
                      .item-description {
                        display: -webkit-box;
                        -webkit-box-orient: vertical;
                        -webkit-line-clamp: 1;
                        overflow: hidden;
                        font-size: 13px;
                        color: #666;
                      }
                
                      .item-price {
                        text-align: right;
                        font-weight: bold;
                        color: #2c3e50;
                      }
                
                      .quantity {
                        text-align: center;
                        color: #666;
                      }
                
                      .totals-section {
                        background: #f8f9fa;
                        padding: 25px 30px;
                      }
                
                      .total-row {
                        display: table;
                        width: 100%;
                        margin-bottom: 8px;
                      }
                
                      .total-label,
                      .total-value {
                        display: table-cell;
                      }
                
                      .total-label {
                        color: #666;
                        font-size: 15px;
                      }
                
                      .total-value {
                        text-align: right;
                        color: #2c3e50;
                        font-weight: bold;
                        font-size: 15px;
                      }
                
                      .grand-total {
                        border-top: 2px solid #dee2e6;
                        padding-top: 15px;
                        margin-top: 15px;
                      }
                
                      .grand-total .total-label,
                      .grand-total .total-value {
                        font-size: 18px;
                        font-weight: bold;
                        color: #ff6b6b;
                      }
                
                      .payment-info {
                        padding: 25px 30px;
                        background: #ffffff;
                        border-top: 1px solid #e9ecef;
                      }
                
                      .payment-method {
                        background: #e8f5e8;
                        padding: 15px;
                        border-radius: 6px;
                        border-left: 4px solid #28a745;
                      }
                
                      .support-section {
                        padding: 25px 30px;
                        text-align: center;
                        background: #f8f9fa;
                      }
                
                      .support-section h3 {
                        color: #2c3e50;
                        margin-bottom: 15px;
                      }
                
                      .support-section p {
                        color: #666;
                        margin-bottom: 15px;
                      }
                
                      .support-button {
                        display: inline-block;
                        background: #ff6b6b;
                        color: white;
                        text-decoration: none;
                        padding: 12px 25px;
                        border-radius: 25px;
                        font-weight: bold;
                        font-size: 14px;
                      }
                
                      .footer {
                        background: #2c3e50;
                        color: white;
                        padding: 25px 30px;
                        text-align: center;
                      }
                
                      .footer p {
                        margin-bottom: 10px;
                        opacity: 0.8;
                        font-size: 14px;
                      }
                
                      @media (max-width: 600px) {
                        body {
                          padding: 10px;
                        }
                
                        .email-container {
                          border-radius: 0;
                        }
                
                        .header,
                        .receipt-header,
                        .customer-info,
                        .totals-section,
                        .payment-info,
                        .support-section,
                        .footer {
                          padding: 20px 15px;
                        }
                
                        .receipt-left,
                        .receipt-right {
                          display: block;
                          width: 100%;
                          text-align: left;
                        }
                
                        .receipt-right {
                          margin-top: 15px;
                        }
                
                        .items-header th,
                        .item-row td {
                          padding: 10px 8px;
                          font-size: 13px;
                        }
                
                        .header h1 {
                          font-size: 24px;
                        }
                      }
                    </style>
                  </head>
                  <body>
                    <div class="email-container">
                      <div class="header">
                        <h1>CheapDeals.com</h1>
                        <div class="receipt-title">Order Receipt</div>
                      </div>
                
                      <div class="receipt-header">
                        <div class="receipt-info">
                          <div class="receipt-left">
                            <div class="order-number">Order %s</div>
                            <div class="order-date">Placed on %s</div>
                          </div>
                          <div class="receipt-right">
                            <div style="color: #28a745; font-weight: bold; font-size: 16px">
                              ✓ Order Confirmed
                            </div>
                            <div style="color: #666; font-size: 14px; margin-top: 4px">
                              Estimated delivery: %s
                            </div>
                          </div>
                        </div>
                      </div>
                
                      <div class="customer-info">
                        <div style="display: table; width: 100%">
                          <div
                            style="
                              display: table-cell;
                              width: 50%;
                              vertical-align: top;
                              padding-right: 20px;
                            "
                          >
                            <div class="info-section">
                              <div class="info-title">Billing Address</div>
                              <div class="info-content">
                                %s
                              </div>
                            </div>
                          </div>
                          <div
                            style="
                              display: table-cell;
                              width: 50%;
                              vertical-align: top;
                              padding-left: 20px;
                            "
                          >
                            <div class="info-section">
                              <div class="info-title">Shipping Address</div>
                              <div class="info-content">
                                %s
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                
                      <table class="items-table">
                        <thead class="items-header">
                          <tr>
                            <th style="width: 50%">Item</th>
                            <th style="width: 15%; text-align: center">Qty</th>
                            <th style="width: 20%; text-align: right">Unit Price</th>
                            <th style="width: 15%; text-align: right">Total</th>
                          </tr>
                        </thead>
                        <tbody>%s</tbody>
                      </table>
                
                      <div class="totals-section">%s</div>
                
                      <div class="payment-info">
                        <div class="info-title">Payment Method</div>
                        <div class="payment-method">
                          <strong style="color: #28a745">✓ Payment Successful</strong><br />
                          Visa ending in 1234<br />
                          Transaction ID: TXN789456123
                        </div>
                      </div>
                
                      <div class="support-section">
                        <h3>Need Help?</h3>
                        <p>
                          Questions about your order? Our customer support team is here to help!
                        </p>
                        <a href="mailto:cheapdeal466@gmail.com" class="support-button"
                          >Contact Support</a
                        >
                      </div>
                
                      <div class="footer">
                        <p><strong>CheapDeals.com LTD</strong></p>
                        <p>Thank you for choosing CheapDeals.com for your shopping needs!</p>
                        <p style="margin-top: 15px; font-size: 12px">
                          This is an automated receipt for your records. Please keep this email
                          for your purchase history.
                        </p>
                      </div>
                    </div>
                  </body>
                </html>
               
                
                """;
        String safeHtmlContent = htmlContent.replace("%", "%%").replace("%%s", "%s");
        String formattedHtmlContent = String.format(
                safeHtmlContent,
                orderId,
                orderDate,
                estimatedDelivery,
                billingAddress,
                shippingAddress,
                itemTable,
                totalSection
        );
        
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromEmail);
        helper.setTo(payment.getUser().getEmail());
        helper.setSubject("Your Order Receipt - CheapDeals.com");
        helper.setText(formattedHtmlContent, true);
        mailSender.send(message);
    }

    private StringBuilder getStringBuilder(Payment payment) {
        List<OrderItem> items = payment.getOrder().getOrderItems(); // Assuming this is a list of items in the order
        StringBuilder itemTable = new StringBuilder();

        for (OrderItem item : items) {
            itemTable.append(generateTableRow(
                    item.getProduct().getName(),
                    String.valueOf(item.getProduct().getDescription()),
                    item.getQuantity(),
                    String.format("$%.2f", item.getPrice()),
                    String.format("$%.2f", 10.24)
            ));
        }
        return itemTable;
    }

    private String generateTotalSection(String subtotal, String discount, String totalPaid) {
        return """
                <div class="totals-section">
                    <div class="total-row">
                        <div class="total-label">Subtotal:</div>
                        <div class="total-value">%s</div>
                    </div>
                    <div class="total-row">
                        <div class="total-label">Discount (SAVE15):</div>
                        <div class="total-value" style="color: #28a745">-%s</div>
                    </div>
                    <div class="total-row grand-total">
                        <div class="total-label">Total Paid:</div>
                        <div class="total-value">%s</div>
                    </div>
                </div>
                """.formatted(subtotal, discount, totalPaid);
    }

    private String generateTableRow(String itemName, String itemDescription, int quantity, String price, String total) {
        String itemTable = """
                <tr class="item-row">
                    <td>
                      <div class="item-name">%s</div>
                      <div class="item-description">
                        %s
                      </div>
                    </td>
                    <td class="quantity">%d</td>
                    <td class="item-price">%s</td>
                    <td class="item-price">%s</td>
                </tr>
                """;
        return String.format(itemTable, itemName, itemDescription, quantity, price, total);
    }
}