package com.greenwich.ecommerce.infra.security;

import com.greenwich.ecommerce.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Slf4j
public class SecurityUserDetails implements UserDetails {

    private Long id;
    private String email;
    private String phone;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private User user;


    public static SecurityUserDetails build(User user, List<GrantedAuthority> authorityList) {
        return SecurityUserDetails.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(authorityList)
                .user(user)
                .build();
    }


    public static SecurityUserDetails build(User user) {
        return SecurityUserDetails.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("Returning authorities from SecurityUserDetails: {}", authorities);
        return this.authorities == null ? List.of() : this.authorities;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
}
