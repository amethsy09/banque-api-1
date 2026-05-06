package com.ecole221.banque_api.security;

import com.ecole221.banque_api.models.AppUser;
import com.ecole221.banque_api.models.Compte;
import com.ecole221.banque_api.services.AppUserService;
import com.ecole221.banque_api.services.CompteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("authorizationHelper")
@RequiredArgsConstructor
public class AuthorizationHelper {

    private final AppUserService appUserService;
    private final CompteService compteService;

    public boolean canAccessClient(Integer clientId) {
        if (isAdmin()) {
            return true;
        }

        return getCurrentUser()
                .map(user -> user.getClient() != null && clientId.equals(user.getClient().getId()))
                .orElse(false);
    }

    public boolean canAccessCompteById(Integer compteId) {
        if (isAdmin()) {
            return true;
        }

        return compteService.findById(compteId)
                .map(this::isOwner)
                .orElse(false);
    }

    public boolean canAccessCompteByNumero(String numero) {
        if (isAdmin()) {
            return true;
        }

        return compteService.findByNumero(numero)
                .map(this::isOwner)
                .orElse(false);
    }

    private boolean isOwner(Compte compte) {
        return getCurrentUser()
                .map(user -> user.getClient() != null
                        && compte.getClient() != null
                        && user.getClient().getId().equals(compte.getClient().getId()))
                .orElse(false);
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }

    private java.util.Optional<AppUser> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return java.util.Optional.empty();
        }

        return appUserService.findByUsername(authentication.getName());
    }
}
