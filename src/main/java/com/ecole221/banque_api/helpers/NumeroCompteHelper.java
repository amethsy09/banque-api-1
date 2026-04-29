package com.ecole221.banque_api.helpers;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class NumeroCompteHelper {

    /**
     * Génère un numéro de compte unique au format :
     * BQ-YYYYMMDD-XXXXXXXX (ex: BQ-20250101-A1B2C3D4)
     */
    public String generer() {
        String datePart = LocalDate.now().toString().replace("-", "");
        String uniquePart = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "BQ-" + datePart + "-" + uniquePart;
    }
}