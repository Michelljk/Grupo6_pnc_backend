package com.example.torneos.repository;

import com.example.torneos.entity.PremioTorneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PremioTorneoRepository extends JpaRepository<PremioTorneo, Long> {
    List<PremioTorneo> findByTorneoIdOrderByPosicionAsc(Long torneoId);
    Optional<PremioTorneo> findByTorneoIdAndPosicion(Long torneoId, Integer posicion);
    List<PremioTorneo> findByUsuarioGanadorId(Long usuarioId);
}
