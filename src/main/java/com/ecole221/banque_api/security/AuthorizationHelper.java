package com.ecole221.banque_api.security;

import com.ecole221.banque_api.models.AppUser;
import com.ecole221.banque_api.models.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("authorizationHelper")
@RequiredArgsConstructor
public class AuthorizationHelper {

    private final Reservation reservationService;

    public boolean canAccessClient(Integer clientId) {
        if (isAdmin()) {
            return true;
        }

        return getCurrentUser()
                .map(user -> user.getClient() != null && clientId.equals(user.getClient().getId()))
                .orElse(false);
    }

    public boolean canAccessCompteById(Integer reservationId) {
        if (isAdmin()) {
            return true;
        }

        return reservationService.findById(reservationId)
                .map(this::isOwner)
                .orElse(false);
    }

    public boolean canAccessCompteByNumero(String numero) {
        if (isAdmin()) {
            return true;
        }

        return reservationService.findByNumero(numero)
                .map(this::isOwner)
                .orElse(false);
    }

    private boolean isOwner(Reservation reservation) {
        return getCurrentUser()
                .map(user -> user.getClient() != null
                        && reservation.getClient() != null
                        && user.getClient().getId().equals(reservation.getClient().getId()))
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

    private Optional<AppUser> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof AppUser appUser) {
            return Optional.of(appUser);
        }

        return Optional.empty();
    }
}
