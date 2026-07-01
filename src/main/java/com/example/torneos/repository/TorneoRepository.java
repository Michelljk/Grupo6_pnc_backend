package com.example.torneos.repository;

import com.example.torneos.entity.Torneo;
import com.example.torneos.enums.EstadoTorneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TorneoRepository extends JpaRepository<Torneo, Long> {
    List<Torneo> findByEstado(EstadoTorneo estado);
    List<Torneo> findByEsPremium(Boolean esPremium);
}
