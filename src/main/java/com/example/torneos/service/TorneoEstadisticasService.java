package com.example.torneos.service;

import com.example.torneos.entity.Resultado;
import com.example.torneos.entity.Torneo;
import com.example.torneos.entity.Usuario;
import com.example.torneos.repository.ResultadoRepository;
import com.example.torneos.repository.TorneoRepository;
import com.example.torneos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TorneoEstadisticasService {

    private final UsuarioRepository usuarioRepository;
    private final TorneoRepository torneoRepository;
    private final ResultadoRepository resultadoRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> obtenerEstadisticasJugador(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        List<Resultado> resultados = resultadoRepository.findAll().stream()
                .filter(r -> participoEnResultado(r, usuario))
                .collect(Collectors.toList());

        long victorias = resultados.stream()
                .filter(r -> usuario.equals(r.getGanador()))
                .count();

        long derrotas = resultados.size() - victorias;

        double porcentajeVictorias = resultados.isEmpty() ? 0 : (victorias * 100.0) / resultados.size();

        Map<String, Object> stats = new HashMap<>();
        stats.put("usuarioId", usuarioId);
        stats.put("nombre", usuario.getNombre());
        stats.put("email", usuario.getEmail());
        stats.put("eloRating", usuario.getEloRating());
        stats.put("creditosVirtuales", usuario.getCreditosVirtuales());
        stats.put("totalPartidos", resultados.size());
        stats.put("victorias", victorias);
        stats.put("derrotas", derrotas);
        stats.put("porcentajeVictorias", String.format("%.2f%%", porcentajeVictorias));

        return stats;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> obtenerEstadisticasJugadorEnTorneo(Long usuarioId, Long torneoId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new IllegalArgumentException("Torneo no encontrado"));

        // Obtener todos los resultados del torneo donde participo el usuario
        List<Resultado> resultados = resultadoRepository.findAll().stream()
                .filter(r -> torneo.equals(obtenerTorneoDeResultado(r)) && participoEnResultado(r, usuario))
                .collect(Collectors.toList());

        long victorias = resultados.stream()
                .filter(r -> usuario.equals(r.getGanador()))
                .count();

        long derrotas = resultados.size() - victorias;

        double porcentajeVictorias = resultados.isEmpty() ? 0 : (victorias * 100.0) / resultados.size();

        Map<String, Object> stats = new HashMap<>();
        stats.put("usuarioId", usuarioId);
        stats.put("torneoId", torneoId);
        stats.put("nombre", usuario.getNombre());
        stats.put("nombreTorneo", torneo.getNombre());
        stats.put("totalPartidos", resultados.size());
        stats.put("victorias", victorias);
        stats.put("derrotas", derrotas);
        stats.put("porcentajeVictorias", String.format("%.2f%%", porcentajeVictorias));

        return stats;
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerHistorialTorneosJugador(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        return torneoRepository.findAll().stream()
                .filter(t -> t.getParticipantes().contains(usuario))
                .map(t -> {
                    Map<String, Object> torneoInfo = new HashMap<>();
                    torneoInfo.put("torneoId", t.getId());
                    torneoInfo.put("nombre", t.getNombre());
                    torneoInfo.put("juego", t.getJuego());
                    torneoInfo.put("formato", t.getFormato());
                    torneoInfo.put("estado", t.getEstado());
                    torneoInfo.put("fechaInicio", t.getFechaInicio());
                    torneoInfo.put("fechaFin", t.getFechaFin());

                    // Calcular desempeño en este torneo
                    List<Resultado> resultados = resultadoRepository.findAll().stream()
                            .filter(r -> t.equals(obtenerTorneoDeResultado(r)) && participoEnResultado(r, usuario))
                            .collect(Collectors.toList());

                    long victorias = resultados.stream()
                            .filter(r -> usuario.equals(r.getGanador()))
                            .count();

                    torneoInfo.put("totalPartidos", resultados.size());
                    torneoInfo.put("victorias", victorias);
                    torneoInfo.put("derrotas", resultados.size() - victorias);

                    return torneoInfo;
                })
                .collect(Collectors.toList());
    }

    // Métodos auxiliares null-safe para evitar NullPointerException
    private boolean participoEnResultado(Resultado r, Usuario usuario) {
        if (r == null || r.getEnfrentamiento() == null) {
            return false;
        }
        boolean esGanador = usuario.equals(r.getGanador());
        boolean esParticipante1 = usuario.equals(r.getEnfrentamiento().getParticipante1());
        boolean esParticipante2 = usuario.equals(r.getEnfrentamiento().getParticipante2());
        return esGanador || esParticipante1 || esParticipante2;
    }

    private Torneo obtenerTorneoDeResultado(Resultado r) {
        if (r == null || r.getEnfrentamiento() == null ||
                r.getEnfrentamiento().getRonda() == null ||
                r.getEnfrentamiento().getRonda().getBracket() == null) {
            return null;
        }
        return r.getEnfrentamiento().getRonda().getBracket().getTorneo();
    }
}
