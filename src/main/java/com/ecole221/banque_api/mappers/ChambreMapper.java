package com.ecole221.banque_api.mappers;

import com.ecole221.banque_api.dto.ChambreDto;
import com.ecole221.banque_api.models.Chambre;
import org.springframework.stereotype.Component;

@Component
public class ChambreMapper {

    public ChambreDto toDto(Chambre chambre) {
        if (chambre == null) return null;
        ChambreDto dto = new ChambreDto();
        dto.setId(chambre.getId());
        dto.setDate(chambre.getDate());
        if (chambre.getType() != null) {
            dto.setTypeLibelle(chambre.getType().getLibelle());
        }
        if (chambre.getReservation() != null) {
            dto.setCompteNumero(chambre.getReservation().getNumero());
        }
        return dto;
    }
}
