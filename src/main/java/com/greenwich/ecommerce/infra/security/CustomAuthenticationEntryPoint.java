//package com.greenwich.ecommerce.infra.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.greenwich.ecommerce.common.enums.ErrorCode;
//import com.greenwich.ecommerce.dto.response.ResponseData;
//import com.greenwich.ecommerce.exception.UnauthorizedException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
////    @Override
////    public void commence(
////            HttpServletRequest request,
////            HttpServletResponse response,
////            AuthenticationException authException
////    ) throws IOException {
////        response.setContentType("application/json");
////        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
////
////        String body = """
////            {
////                "status": false,
////                "message": "Bạn chưa đăng nhập hoặc token không hợp lệ"
////            }
////        """;
////
////        response.getWriter().write(body);
////    }
//
//    @Override
//    public void commence(HttpServletRequest request,
//                         HttpServletResponse response,
//                         AuthenticationException authException) throws IOException {
//
//        response.setContentType("application/json");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//
//        UnauthorizedException ex = new UnauthorizedException(request.getRequestURI());
//
//        ResponseData<Object> errorResponse = ResponseData.builder()
//                .status(Integer.parseInt(ErrorCode.UNAUTHORIZED.getCode()))
//                .message(ex.getFormattedMessage())
//                .data(null)
//                .build();
//
//        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
//    }
//}
