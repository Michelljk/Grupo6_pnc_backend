package com.example.torneos.service;
import com.example.torneos.dto.request.RegistrarResultadoRequest;
import com.example.torneos.entity.*;
import com.example.torneos.enums.EstadoEnfrentamiento;
import com.example.torneos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResultadoService {
    private final EnfrentamientoRepository enfrentamientoRepository;
    private final ResultadoRepository resultadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final BracketService bracketService;

    @Transactional
    public Resultado registrarResultado(Long matchId, RegistrarResultadoRequest request) {
        Enfrentamiento match = enfrentamientoRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Enfrentamiento no encontrado"));
        Usuario ganador = usuarioRepository.findById(request.getGanadorId())
                .orElseThrow(() -> new IllegalArgumentException("Ganador no encontrado"));

        // Validar que el ganador sea uno de los participantes
        if (!ganador.equals(match.getParticipante1()) && !ganador.equals(match.getParticipante2())) {
            throw new IllegalArgumentException("El ganador debe ser uno de los participantes del enfrentamiento");
        }

        // Validar que el enfrentamiento no este ya finalizado
        if (match.getEstado() == EstadoEnfrentamiento.FINALIZADO) {
            throw new IllegalStateException("El enfrentamiento ya ha sido finalizado");
        }

        // Validar consistencia de scores
        if (ganador.equals(match.getParticipante1())) {
            if (request.getScoreP1() < request.getScoreP2()) {
                throw new IllegalArgumentException("El score del ganador debe ser mayor o igual al del perdedor");
            }
        } else {
            if (request.getScoreP2() < request.getScoreP1()) {
                throw new IllegalArgumentException("El score del ganador debe ser mayor o igual al del perdedor");
            }
        }

        match.setGanador(ganador);
        match.setScoreParticipante1(request.getScoreP1());
        match.setScoreParticipante2(request.getScoreP2());
        match.setEstado(EstadoEnfrentamiento.FINALIZADO);
        enfrentamientoRepository.save(match);

        Resultado resultado = Resultado.builder()
                .enfrentamiento(match)
                .ganador(ganador)
                .scoreParticipante1(request.getScoreP1())
                .scoreParticipante2(request.getScoreP2())
                .evidenciaUrl(request.getEvidenciaUrl())
                .validadoPorAdmin(false)
                .build();
        Resultado resultadoGuardado = resultadoRepository.save(resultado);

        // Intentar avanzar el bracket si todos los enfrentamientos de la ronda estan finalizados
        try {
            Ronda ronda = match.getRonda();
            if (ronda != null && ronda.getBracket() != null) {
                bracketService.avanzarBracketSiEsPosible(ronda.getBracket().getId());
            }
        } catch (Exception e) {
            // Log silencioso, no interrumpir el registro del resultado
            System.err.println("Error al intentar avanzar el bracket: " + e.getMessage());
        }

        return resultadoGuardado;
    }
}
