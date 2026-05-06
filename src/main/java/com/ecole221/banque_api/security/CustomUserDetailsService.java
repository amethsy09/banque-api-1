package com.ecole221.banque_api.security;

import com.ecole221.banque_api.services.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserService appUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserService.findByUsername(username)
                .map(user -> User.withUsername(user.getUsername())
                        .password(user.getPassword())
                        .authorities(user.getRole())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable: " + username));
    }
}
