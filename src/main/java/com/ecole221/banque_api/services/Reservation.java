package com.ecole221.banque_api.services;

import com.ecole221.banque_api.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class Reservation {

    private final ReservationRepository reservationRepository;

    public Reservation(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public boolean existsByNumero(String numero) {
        return reservationRepository.existsByNumero(numero);
    }

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findByClientId(Integer clientId) {
        return reservationRepository.findByClientId(clientId);
    }

    @Transactional(readOnly = true)
    public Optional<Reservation> findByNumero(String numero) {
        return reservationRepository.findByNumero(numero);
    }

    @Transactional(readOnly = true)
    public Optional<Reservation> findById(Integer id) {
        return reservationRepository.findById(id);
    }
}
