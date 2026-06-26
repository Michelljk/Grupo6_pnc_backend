package com.example.torneos.service;
import com.example.torneos.entity.*;
import com.example.torneos.enums.FormatoTorneo;
import com.example.torneos.repository.*;
import com.example.torneos.service.strategy.FormatoStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class BracketService {
    private final BracketRepository bracketRepository;
    private final TorneoRepository torneoRepository;
    private final Map<FormatoTorneo, FormatoStrategy> formatStrategies;

    @Transactional
    public Bracket iniciarTorneo(Long torneoId) {
        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new IllegalArgumentException("Torneo no encontrado con ID: " + torneoId));

        FormatoStrategy strategy = formatStrategies.get(torneo.getFormato());

        if (strategy == null) {
            throw new UnsupportedOperationException("El formato de torneo " + torneo.getFormato() + " no tiene una estrategia implementada.");
        }

        // Validar que el torneo tenga participantes
        if (torneo.getParticipantes() == null || torneo.getParticipantes().isEmpty()) {
            throw new IllegalStateException("No se puede iniciar un torneo sin participantes.");
        }

        Bracket bracket = strategy.generarBracketInicial(torneo, torneo.getParticipantes());

        if (bracket == null) {
            throw new IllegalStateException("La estrategia para " + torneo.getFormato() + " devolvió un bracket nulo.");
        }

        return bracketRepository.save(bracket);
    }

    @Transactional
    public void avanzarRonda(Long bracketId) {
        Bracket bracket = bracketRepository.findById(bracketId)
                .orElseThrow(() -> new IllegalArgumentException("Bracket no encontrado con ID: " + bracketId));

        FormatoStrategy strategy = formatStrategies.get(bracket.getTorneo().getFormato());

        if (strategy == null) {
            throw new UnsupportedOperationException("No hay estrategia para el formato " + bracket.getTorneo().getFormato());
        }

        strategy.avanzarRonda(bracket);
        bracketRepository.save(bracket);
    }
}
