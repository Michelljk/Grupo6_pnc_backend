package com.example.torneos.repository;

import com.example.torneos.entity.PuntoJugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuntoJugadorRepository extends JpaRepository<PuntoJugador, Long> {
    List<PuntoJugador> findByUsuarioId(Long usuarioId);
    List<PuntoJugador> findByTorneoOrigenId(Long torneoId);

    @Query("SELECT p.usuario.id, p.usuario.nombre, SUM(p.puntos) as totalPuntos " +
           "FROM PuntoJugador p GROUP BY p.usuario.id, p.usuario.nombre ORDER BY totalPuntos DESC")
    List<Object[]> calcularRankingGlobal();

    @Query("SELECT COALESCE(SUM(p.puntos), 0) FROM PuntoJugador p WHERE p.usuario.id = :usuarioId")
    Integer sumPuntosByUsuarioId(Long usuarioId);
}
