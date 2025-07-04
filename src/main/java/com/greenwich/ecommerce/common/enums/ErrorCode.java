package com.greenwich.ecommerce.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@RequiredArgsConstructor
@Slf4j
public enum ErrorCode {
    NOT_FOUND("404", "%s not found"),
    BAD_REQUEST("400", "%s is invalid"),
    UNAUTHORIZED("401", "Unauthorized access to %s"),
    VALIDATION_ERROR("400", "Validation error: %s"),
    CONFLICT("409", "%s already exists"),
    FORBIDDEN("403", "Access to %s is forbidden"),
    INVALID_TOKEN("401", "Token is invalid or expired"),
    RATE_LIMIT_EXCEEDED("429", "Rate limit exceeded for %s"),
    INTERNAL_ERROR("500", "Error happened while processing your request, contact admin for more info"),
    INVALID_ARGUMENT("400", "Invalid argument: %s - %s"),
    BAD_CREDENTIAL_LOGIN("401", "Invalid username or password"),
    UNAUTHORIZED_ACCESS("403", "Access denied"),
    INVALID_REQUEST("400", "Invalid request: %s"),
    IO_ERROR("400", "IO error: %s"),
    ACCESS_DENIED("403", "You don't have permission to access this resource"),
    REDIS_CONNECTION_ERROR("503", "Service temporarily unavailable due to Redis connection failure"),
    AUTHENTICATION_SERVICE_ERROR("401", "Authentication service error, please try again later"),
    SERVICE_UNAVAILABLE("503", "Service temporarily unavailable, please try again later"),;

    private final String code;
    private final String messageTemplate;
    public String formatMessage(Object... args) {
        log.info("formatMessage called: code={}, template={}, args={}",
                code, messageTemplate, Arrays.toString(args));

        if (args == null || args.length == 0) {
            log.warn("No args provided for template: {}", messageTemplate);
            return messageTemplate;
        }

        int expectedArgs = countPlaceholders(messageTemplate);
        if (args.length < expectedArgs) {
            log.error("Insufficient arguments: expected={}, provided={}, template={}, args={}",
                    expectedArgs, args.length, messageTemplate, Arrays.toString(args));
            return String.valueOf(args[0]);
        }

        try {
            String result = String.format(messageTemplate, args);
            log.info("formatMessage success: result={}", result);
            return result;
        } catch (Exception e) {
            log.error("formatMessage failed: template={}, args={}, error={}",
                    messageTemplate, Arrays.toString(args), e.getMessage(), e);
            e.printStackTrace(System.err);
            return String.valueOf(args[0]);
        }
    }

    private int countPlaceholders(String template) {
        if (template == null) return 0;
        Pattern pattern = Pattern.compile("%s");
        Matcher matcher = pattern.matcher(template);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}