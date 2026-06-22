package com.example.torneos.service;

import com.example.torneos.dto.PuntoJugadorDTO;
import com.example.torneos.dto.RankingDTO;
import com.example.torneos.entity.PuntoJugador;
import com.example.torneos.entity.Torneo;
import com.example.torneos.entity.Usuario;
import com.example.torneos.exception.ResourceNotFoundException;
import com.example.torneos.repository.PuntoJugadorRepository;
import com.example.torneos.repository.TorneoRepository;
import com.example.torneos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PuntoService {

    private final PuntoJugadorRepository puntoJugadorRepository;
    private final UsuarioRepository usuarioRepository;
    private final TorneoRepository torneoRepository;

    @Transactional
    public PuntoJugadorDTO otorgarPuntos(Long usuarioId, Integer puntos, Long torneoId, String descripcion) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + usuarioId));

        PuntoJugador punto = new PuntoJugador();
        punto.setUsuario(usuario);
        punto.setPuntos(puntos);
        punto.setDescripcion(descripcion);

        if (torneoId != null) {
            Torneo torneo = torneoRepository.findById(torneoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Torneo no encontrado: " + torneoId));
            punto.setTorneoOrigen(torneo);
        }

        return toDTO(puntoJugadorRepository.save(punto));
    }

    @Transactional
    public PuntoJugadorDTO registrarPuntos(PuntoJugadorDTO dto) {
        return otorgarPuntos(dto.getUsuarioId(), dto.getPuntos(), dto.getTorneoId(), dto.getDescripcion());
    }

    @Transactional(readOnly = true)
    public List<PuntoJugadorDTO> obtenerPorJugador(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuario no encontrado: " + usuarioId);
        }
        return puntoJugadorRepository.findByUsuarioId(usuarioId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Integer obtenerTotalPuntos(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuario no encontrado: " + usuarioId);
        }
        return puntoJugadorRepository.sumPuntosByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<PuntoJugadorDTO> obtenerPorTorneo(Long torneoId) {
        if (!torneoRepository.existsById(torneoId)) {
            throw new ResourceNotFoundException("Torneo no encontrado: " + torneoId);
        }
        return puntoJugadorRepository.findByTorneoOrigenId(torneoId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RankingDTO> obtenerRankingGlobal() {
        List<Object[]> resultados = puntoJugadorRepository.calcularRankingGlobal();
        AtomicInteger posicion = new AtomicInteger(1);
        List<RankingDTO> ranking = new ArrayList<>();
        for (Object[] row : resultados) {
            ranking.add(new RankingDTO(
                    ((Number) row[0]).longValue(),
                    (String) row[1],
                    ((Number) row[2]).intValue(),
                    posicion.getAndIncrement()
            ));
        }
        return ranking;
    }

    private PuntoJugadorDTO toDTO(PuntoJugador punto) {
        PuntoJugadorDTO dto = new PuntoJugadorDTO();
        dto.setId(punto.getId());
        dto.setUsuarioId(punto.getUsuario().getId());
        dto.setPuntos(punto.getPuntos());
        dto.setDescripcion(punto.getDescripcion());
        if (punto.getTorneoOrigen() != null) {
            dto.setTorneoId(punto.getTorneoOrigen().getId());
        }
        return dto;
    }
}
