package com.greenwich.ecommerce.infra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenwich.ecommerce.repository.UserRepository;
import com.greenwich.ecommerce.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

//    private final UserRepository userRepo;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public static final String[] PUBLIC_LIST = {
            "/api/v1/users/login",
            "/api/v1/users/register",
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//                .cors(Customizer.withDefaults()) // Spring tự dùng corsConfigurationSource()
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/**", "/api/v1/users/register",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-ui/index.html").permitAll()
                        .anyRequest().authenticated()
                );
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
//
//    @Bean
//    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOriginPatterns(List.of("*"));
//        config.setAllowedMethods(List.of("*"));
//        config.setAllowedHeaders(List.of("*"));
//        config.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
}
//@Slf4j
//public class SecurityConfig {
//
//    public static final String[] WHITE_LIST = {
//            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/index.html"
//    };
//
//    public static final String[] PUBLIC_LIST = {
//            // Authentication
//            "/api/v1/users/login",
//            "/api/v1/users/register",
//    };
//
//    public static final String[] CUSTOMER_LIST = {
//            "/api/v1/users/current",
//            "/api/v1/users/current/**",
//            "/api/v1/users/logout",
//            "/api/v1/products/{id}/rate"
//    };
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        log.info("Configuring AuthenticationManager");
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http.cors(cors -> {
//                    log.info("Configuring CORS with source: {}", corsConfigurationSource());
//                    cors.configurationSource(corsConfigurationSource());
//                })
//                .formLogin(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> {
//                    log.info("Setting session management to STATELESS");
//                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                })
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // nếu dùng JWT
//                )
//                .authorizeHttpRequests(requests -> {
//                    log.info("Configuring authorization rules");
//                    requests
////                            .requestMatchers(WHITE_LIST).permitAll()
//                            .requestMatchers(PUBLIC_LIST).permitAll()
//                            .anyRequest().authenticated();
//                });
//        ;
//
//
//        log.info("Adding authentication provider");
//        http.authenticationProvider(authenticationProvider());
//
//        log.info("Adding JwtAuthenticationFilter before UsernamePasswordAuthenticationFilter");
//        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        log.info("SecurityFilterChain configuration completed");
//        return http.build();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService(UserRepository userRepo) {
//        return username -> userRepo.findByEmail(username)
//                .map(user -> {
//                    List<GrantedAuthority> authorities =
//                            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().toUpperCase()));
//                    return new SecurityUserDetails(user, authorities);
//                })
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    }
//
//
//
//    @Bean
//    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration corsConfig = new CorsConfiguration();
//        corsConfig.addAllowedOrigin("http://localhost:5173");
//        corsConfig.addAllowedMethod("*");
//        corsConfig.addAllowedHeader("*");
//        corsConfig.addAllowedOriginPattern("*");
//        corsConfig.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfig);
//        log.info("Configured CORS: allowed origins={}, methods={}, headers={}, credentials={}",
//                corsConfig.getAllowedOrigins(), corsConfig.getAllowedMethods(),
//                corsConfig.getAllowedHeaders(), corsConfig.getAllowCredentials());
//        return source;
//    }
//}


// CORS config chính thống cho Security
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:80")); // domain FE
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
//        config.setAllowedHeaders(List.of("*"));
//        config.setAllowCredentials(true);
//        config.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config); // Áp dụng toàn bộ route
//        return source;
//    }


//                            .requestMatchers(HttpMethod.GET, "/error").permitAll()
//                            .requestMatchers(HttpMethod.GET, "/api/v1/products").permitAll()
//                            .requestMatchers(HttpMethod.GET, "/api/v1/products/featured").permitAll()
//                            .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
//                            .requestMatchers(HttpMethod.POST, "/api/v1/products").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.GET, "/api/v1/categories").permitAll()
//                            .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
//                            .requestMatchers(HttpMethod.POST, "/api/v1/categories").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.PUT, "/api/v1/categories/**").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.DELETE, "/api/v1/categories/**").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.GET, "/api/v1/users").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.POST, "/api/v1/users").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole("ADMIN")
//                            .requestMatchers(HttpMethod.GET, "/api/v1/users/current").hasAnyRole("CUSTOMER", "ADMIN")
//                            .requestMatchers(HttpMethod.PUT,"/api/v1/users/current/**").hasAnyRole("CUSTOMER", "ADMIN")
//                            .requestMatchers(HttpMethod.POST, "/api/v1/users/logout").hasAnyRole("CUSTOMER", "ADMIN")
//                            .requestMatchers(HttpMethod.POST, "/api/v1/products/{id}/rate").hasRole("CUSTOMER")
//                            .requestMatchers(HttpMethod.POST, "/api/v1/users/verify-otp").hasAnyRole("CUSTOMER", "ADMIN")
//                            .requestMatchers(HttpMethod.POST, "/api/v1/users/change-password").hasAnyRole("CUSTOMER", "ADMIN")