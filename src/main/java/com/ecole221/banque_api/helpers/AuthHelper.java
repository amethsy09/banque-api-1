package com.ecole221.banque_api.helpers;

import com.ecole221.banque_api.dto.AuthRequestDto;
import com.ecole221.banque_api.dto.AuthResponseDto;
import com.ecole221.banque_api.dto.RegisterRequestDto;
import com.ecole221.banque_api.exceptions.ResourceNotFoundException;
import com.ecole221.banque_api.exceptions.TransactionException;
import com.ecole221.banque_api.models.AppUser;
import com.ecole221.banque_api.models.Client;
import com.ecole221.banque_api.services.AppUserService;
import com.ecole221.banque_api.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthHelper {

    private final AppUserService appUserService;
    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;

    public AuthResponseDto register(RegisterRequestDto request) {
        if (appUserService.existsByUsername(request.getUsername())) {
            throw new TransactionException("Ce nom d'utilisateur existe deja.");
        }

        AppUser user = new AppUser();
        String role = normaliserRole(request.getRole());
        Client client = resolveClientForRole(role, request.getClientId());

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.attachClient(client);

        AppUser savedUser = appUserService.save(user);
        UserDetails securityUser = buildSecurityUser(savedUser);

        return new AuthResponseDto(
                jwtHelper.generateToken(securityUser),
                "Bearer",
                savedUser.getUsername(),
                savedUser.getRole());
    }

    public AuthResponseDto login(AuthRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String role = principal.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse("ROLE_USER");

        return new AuthResponseDto(
                jwtHelper.generateToken(principal),
                "Bearer",
                principal.getUsername(),
                role);
    }

    private UserDetails buildSecurityUser(AppUser user) {
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole())
                .build();
    }

    private String normaliserRole(String role) {
        String roleNormalise = role.trim().toUpperCase();
        if (!roleNormalise.startsWith("ROLE_")) {
            roleNormalise = "ROLE_" + roleNormalise;
        }
        return roleNormalise;
    }

    private Client resolveClientForRole(String role, Integer clientId) {
        if ("ROLE_ADMIN".equals(role)) {
            return null;
        }

        if (!"ROLE_USER".equals(role)) {
            throw new TransactionException("Role invalide. Utiliser ADMIN ou USER.");
        }

        if (clientId == null) {
            throw new TransactionException("Le clientId est obligatoire pour un utilisateur USER.");
        }

        if (appUserService.existsByClientId(clientId)) {
            throw new TransactionException("Ce client possede deja un utilisateur.");
        }

        return clientService.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'id: " + clientId));
    }
}
