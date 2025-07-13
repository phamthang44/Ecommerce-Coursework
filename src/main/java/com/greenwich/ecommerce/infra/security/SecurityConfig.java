package com.greenwich.ecommerce.infra.security;

import com.greenwich.ecommerce.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          UserDetailsServiceImpl userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    public static final String[] PUBLIC_LIST = {
            "/api/v1/users/login",
            "/api/v1/users/register",
    };

    public static final String[] WHITE_LIST = {
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/index.html"
    };

//    public static final String[] ADMIN_LIST = {
//            "/api/v1/users/**",
//            "/api/v1/categories/**",
//            "/api/v1/products/**",
//            "/api/v1/orders/**",
//            "/api/v1/roles/**",
//            "/api/v1/users/current/role",
//    };
//
//    public static final String[] CUSTOMER_LIST = {
//            "/api/v1/users/current",
//            "/api/v1/users/current/**",
//            "/api/v1/users/logout",
//            "/api/v1/products/{id}",
//            "/api/v1/products",
//            "/api/v1/cart/items",
//            "/api/v1/cart",
//    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    // Public APIs
                    auth.requestMatchers(PUBLIC_LIST).permitAll();
                    auth.requestMatchers(WHITE_LIST).permitAll();

                    auth.requestMatchers(HttpMethod.GET, "/error").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/products").hasAnyRole("CUSTOMER", "ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/products/{id}").hasAnyRole("CUSTOMER", "ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/users/current").hasAnyRole("CUSTOMER", "ADMIN");
                    auth.requestMatchers(HttpMethod.PUT,"/api/v1/users/current/**").hasAnyRole("CUSTOMER", "ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/products").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/products/{id}").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/products/{id}").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/categories").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/categories/{id}").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/categories").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/categories/{id}").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/categories/{id}").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/users").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/users").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PATCH, "/api/v1/users/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/users/logout").hasAnyRole("CUSTOMER", "ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/cart").hasAnyRole("CUSTOMER", "ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/cart/items").hasAnyRole("CUSTOMER", "ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/cart/items").hasAnyRole("CUSTOMER", "ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/cart/items").hasAnyRole("CUSTOMER", "ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/cart/items/{id}").hasAnyRole("CUSTOMER", "ADMIN");

                    auth.anyRequest().authenticated();
                    }
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}