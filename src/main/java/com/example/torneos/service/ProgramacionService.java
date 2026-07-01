package com.example.torneos.service;

import com.example.torneos.entity.Bracket;
import com.example.torneos.entity.Enfrentamiento;
import com.example.torneos.entity.Ronda;
import com.example.torneos.exception.ResourceNotFoundException;
import com.example.torneos.repository.BracketRepository;
import com.example.torneos.repository.EnfrentamientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramacionService {

    private final BracketRepository bracketRepository;
    private final EnfrentamientoRepository enfrentamientoRepository;

    @Transactional
    public void programarEnfrentamiento(Long enfrentamientoId, LocalDateTime fechaHora) {
        Enfrentamiento enfrentamiento = enfrentamientoRepository.findById(enfrentamientoId)
                .orElseThrow(() -> new ResourceNotFoundException("Enfrentamiento no encontrado con ID: " + enfrentamientoId));

        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha y hora no pueden ser nulas");
        }

        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se puede programar un enfrentamiento en el pasado");
        }

        enfrentamiento.setFechaHora(fechaHora);
        enfrentamientoRepository.save(enfrentamiento);
    }

    @Transactional
    public void programarRonda(Long rondaId, LocalDateTime fechaInicio, int minutosEntreProgramas) {

    }

    @Transactional
    public void programarBracketCompleto(Long bracketId, LocalDateTime fechaInicio, int minutosEntreProgramas) {
        Bracket bracket = bracketRepository.findById(bracketId)
                .orElseThrow(() -> new ResourceNotFoundException("Bracket no encontrado con ID: " + bracketId));

        LocalDateTime fechaActual = fechaInicio;

        for (Ronda ronda : bracket.getRondas()) {
            for (Enfrentamiento enfrentamiento : ronda.getEnfrentamientos()) {
                if (enfrentamiento.getFechaHora() == null) {
                    enfrentamiento.setFechaHora(fechaActual);
                    enfrentamientoRepository.save(enfrentamiento);
                    fechaActual = fechaActual.plusMinutes(minutosEntreProgramas);
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public List<Enfrentamiento> obtenerEnfrentamientosProximos(Long usuarioId, int diasAdelante) {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limite = ahora.plusDays(diasAdelante);

        return enfrentamientoRepository.findAll().stream()
                .filter(e -> {
                    if (e.getFechaHora() == null) return false;
                    return (e.getParticipante1() != null && e.getParticipante1().getId().equals(usuarioId) ||
                            e.getParticipante2() != null && e.getParticipante2().getId().equals(usuarioId)) &&
                            e.getFechaHora().isAfter(ahora) &&
                            e.getFechaHora().isBefore(limite);
                })
                .sorted((e1, e2) -> e1.getFechaHora().compareTo(e2.getFechaHora()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Enfrentamiento> obtenerEnfrentamientosDelDia(Long usuarioId) {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioDia = ahora.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finDia = ahora.withHour(23).withMinute(59).withSecond(59);

        return enfrentamientoRepository.findAll().stream()
                .filter(e -> {
                    if (e.getFechaHora() == null) return false;
                    return (e.getParticipante1() != null && e.getParticipante1().getId().equals(usuarioId) ||
                            e.getParticipante2() != null && e.getParticipante2().getId().equals(usuarioId)) &&
                            e.getFechaHora().isAfter(inicioDia) &&
                            e.getFechaHora().isBefore(finDia);
                })
                .sorted((e1, e2) -> e1.getFechaHora().compareTo(e2.getFechaHora()))
                .toList();
    }
}
