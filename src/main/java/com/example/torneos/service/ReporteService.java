package com.example.torneos.service;

import com.example.torneos.repository.TorneoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final TorneoRepository torneoRepository;

    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerTorneosMasPopulares() {
        return torneoRepository.findAll().stream()
                .sorted((t1, t2) -> Integer.compare(t2.getParticipantes().size(), t1.getParticipantes().size()))
                .limit(5)
                .map(t -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre", t.getNombre());
                    map.put("participantes", t.getParticipantes().size());
                    map.put("juego", t.getJuego());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> obtenerEstadisticasGenerales() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTorneos", torneoRepository.count());
        stats.put("torneosPorJuego", torneoRepository.findAll().stream()
                .collect(Collectors.groupingBy(t -> t.getJuego(), Collectors.counting())));
        return stats;
    }
}
