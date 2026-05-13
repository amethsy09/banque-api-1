package com.ecole221.banque_api.mappers;

import com.ecole221.banque_api.dto.ReservationDto;
import com.ecole221.banque_api.models.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationDto toDto(Reservation reservation) {
        if (reservation == null) return null;
        ReservationDto dto = new ReservationDto();
        dto.setId(reservation.getId());
        dto.setNumero(reservation.getNumero());
        dto.setDateCreation(reservation.getDateCreation());
        if (reservation.getClient() != null) {
            dto.setClientId(reservation.getClient().getId());
            dto.setClientNomComplet(reservation.getClient().getPrenom() + " " + reservation.getClient().getNom());
        }
        return dto;
    }
}
