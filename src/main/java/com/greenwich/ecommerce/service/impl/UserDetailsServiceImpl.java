package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.entity.User;
import com.greenwich.ecommerce.infra.security.SecurityUserDetails;
import com.greenwich.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        User user = (User) userRepository
                .findByEmail(mail)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

        List<GrantedAuthority> authorityList = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().toUpperCase())
        );

        authorityList.forEach(authority -> log.info("Authority: {}", authority));

        return SecurityUserDetails.build(user, authorityList);
    }

//    public User getUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null) return null;
//
//        Object principal = authentication.getPrincipal();
//        log.info("Principal test v1:  {}", principal);
//
//        if (principal instanceof SecurityUserDetails securityUserDetails) {
//            // Return the already fetched user from the authentication context
//            return securityUserDetails.getUser();
//        }
//
//        return null;
//    }


//    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
//        User user = (User) userRepository
//                .findByEmail(mail)
//                .orElseThrow(() -> new NotFoundException("User"));
//
//        List<GrantedAuthority> authorityList = List.of(
//                new SimpleGrantedAuthority(user.getRole().getName())
//        );
//
//        authorityList.forEach(authority -> log.info("Authority: {}", authority));
//
//        return SecurityUserDetails.build(user, authorityList);
//    }
//
    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return Optional.empty();

        Object principal = authentication.getPrincipal();
        log.info("Principal: {}", principal);

        if (principal instanceof SecurityUserDetails securityUserDetails) {
            // Return the already fetched user from the authentication context
            return Optional.of(securityUserDetails.getUser());
        }

        return Optional.empty();
    }
}
