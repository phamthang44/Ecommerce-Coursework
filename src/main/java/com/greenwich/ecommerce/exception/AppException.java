package com.greenwich.ecommerce.exception;

import com.greenwich.ecommerce.common.enums.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@Slf4j
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String formattedMessage;

    public AppException(ErrorCode errorCode, String messageArg) {
//        super(formatMessage(errorCode, messageArg));
//        this.errorCode = errorCode != null ? errorCode : ErrorCode.INTERNAL_ERROR;
//        this.formattedMessage = formatMessage(errorCode, messageArg);
        super(formatMessage(errorCode, messageArg));
        this.errorCode = errorCode != null ? errorCode : ErrorCode.INTERNAL_ERROR;
        this.formattedMessage = formatMessage(errorCode, messageArg);
        log.info("AppException constructor (ErrorCode, String) called: errorCode={}, formattedMessage={}, super.getMessage()={}",
                this.errorCode.getCode(), this.formattedMessage, super.getMessage());
    }

    public AppException(ErrorCode errorCode, Object... messageArgs) {
        super(formatMessage(errorCode, messageArgs));
        this.errorCode = errorCode != null ? errorCode : ErrorCode.INTERNAL_ERROR;
        this.formattedMessage = formatMessage(errorCode, messageArgs);
        log.info("AppException constructor (ErrorCode, Object...) called: errorCode={}, formattedMessage={}, args={}, super.getMessage()={}",
                this.errorCode.getCode(), this.formattedMessage, Arrays.toString(messageArgs), super.getMessage());
    }

    private static String formatMessage(ErrorCode errorCode, Object... args) {
        log.debug("formatMessage called: errorCode={}, args={}",
                errorCode != null ? errorCode.getCode() : "null", Arrays.toString(args));
        if (errorCode == null) {
            log.error("ErrorCode is null, using fallback: args={}", Arrays.toString(args));
            return args != null && args.length > 0 ? String.valueOf(args[0]) : "Unknown error";
        }
        try {
            log.debug("Attempting to call ErrorCode.formatMessage: errorCode={}, args={}",
                    errorCode.getCode(), Arrays.toString(args));
            String result = errorCode.formatMessage(args);
            log.debug("Formatted message success: errorCode={}, result={}", errorCode.getCode(), result);
            return result;
        } catch (Exception e) {
            log.error("Failed to format message: errorCode={}, template={}, args={}, error={}",
                    errorCode.getCode(), errorCode.getMessageTemplate(), Arrays.toString(args), e.getMessage(), e);
            e.printStackTrace(System.err);
            return args != null && args.length > 0 ? String.valueOf(args[0]) : errorCode.getMessageTemplate();
        }
    }

    public String getErrorCodeString() {
        return errorCode.getCode();
    }

    public HttpStatus getHttpStatus() {
        return switch (errorCode.getCode()) {
            case "400" -> HttpStatus.BAD_REQUEST;
            case "401" -> HttpStatus.UNAUTHORIZED;
            case "403" -> HttpStatus.FORBIDDEN;
            case "404" -> HttpStatus.NOT_FOUND;
            case "409" -> HttpStatus.CONFLICT;
            case "422" -> HttpStatus.UNPROCESSABLE_ENTITY;
            case "429" -> HttpStatus.TOO_MANY_REQUESTS;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}