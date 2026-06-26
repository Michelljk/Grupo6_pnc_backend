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

    @Transactional
    public Resultado registrarResultado(Long matchId, RegistrarResultadoRequest request) {
        Enfrentamiento match = enfrentamientoRepository.findById(matchId).orElseThrow(() -> new IllegalArgumentException("Enfrentamiento no encontrado"));
        Usuario ganador = usuarioRepository.findById(request.getGanadorId()).orElseThrow(() -> new IllegalArgumentException("Ganador no encontrado"));
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
                .build();
        return resultadoRepository.save(resultado);
    }
}
