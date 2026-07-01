package com.example.torneos.service;

import com.example.torneos.dto.torneo.TorneoRequest;
import com.example.torneos.dto.torneo.TorneoResponse;
import com.example.torneos.entity.Torneo;
import com.example.torneos.entity.Usuario;
import com.example.torneos.enums.EstadoTorneo;
import com.example.torneos.enums.FormatoTorneo;
import com.example.torneos.exception.ResourceNotFoundException;
import com.example.torneos.mapper.TorneoMapper;
import com.example.torneos.repository.TorneoRepository;
import com.example.torneos.repository.UsuarioRepository;
import com.example.torneos.service.strategy.FormatoStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TorneoService {

    private final TorneoRepository torneoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TorneoMapper torneoMapper;
    private final Map<FormatoTorneo, FormatoStrategy> formatStrategies;

    @Transactional
    public TorneoResponse crearTorneo(TorneoRequest request) {
        if (request.getEloMinimo() > request.getEloMaximo()) {
            throw new IllegalArgumentException("El ELO mínimo no puede ser mayor que el ELO máximo");
        }

        Torneo torneo = torneoMapper.toEntity(request);
        torneo.setEstado(EstadoTorneo.PROXIMO);

        FormatoStrategy strategy = formatStrategies.get(torneo.getFormato());
        if (strategy != null) {
            strategy.validarConfiguracion(torneo);
        }

        return torneoMapper.toResponse(torneoRepository.save(torneo));
    }

    @Transactional(readOnly = true)
    public List<TorneoResponse> listarTorneos() {
        return torneoRepository.findAll().stream()
                .map(torneoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TorneoResponse obtenerPorId(Long id) {
        Torneo torneo = torneoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Torneo no encontrado con ID: " + id));
        return torneoMapper.toResponse(torneo);
    }

    @Transactional
    public TorneoResponse actualizarTorneo(Long id, TorneoRequest request) {
        Torneo torneo = torneoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Torneo no encontrado con ID: " + id));

        torneoMapper.updateTorneoFromRequest(request, torneo);

        FormatoStrategy strategy = formatStrategies.get(torneo.getFormato());
        if (strategy != null) {
            strategy.validarConfiguracion(torneo);
        }

        return torneoMapper.toResponse(torneoRepository.save(torneo));
    }

    @Transactional
    public void eliminarTorneo(Long id) {
        if (!torneoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Torneo no encontrado con ID: " + id);
        }
        torneoRepository.deleteById(id);
    }

    @Transactional
    public void inscribirUsuario(Long torneoId, Long usuarioId) {
        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new ResourceNotFoundException("Torneo no encontrado"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Validaciones
        if (torneo.getParticipantes().size() >= torneo.getMaxParticipantes()) {
            throw new IllegalStateException("El torneo ya alcanzó el límite máximo de participantes.");
        }

        if (usuario.getEloRating() < torneo.getEloMinimo() || usuario.getEloRating() > torneo.getEloMaximo()) {
            throw new IllegalStateException("El ELO del usuario no está dentro del rango permitido para este torneo.");
        }

        if (torneo.getParticipantes().contains(usuario)) {
            throw new IllegalStateException("El usuario ya está inscrito en este torneo.");
        }

        torneo.getParticipantes().add(usuario);
        torneoRepository.save(torneo);
        simularNotificacion(usuario.getEmail(), "Inscripción exitosa al torneo: " + torneo.getNombre());
    }

    @Transactional
    public void removerUsuario(Long torneoId, Long usuarioId) {
        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new ResourceNotFoundException("Torneo no encontrado"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!torneo.getParticipantes().contains(usuario)) {
            throw new IllegalStateException("El usuario no está inscrito en este torneo.");
        }

        torneo.getParticipantes().remove(usuario);
        torneoRepository.save(torneo);
        simularNotificacion(usuario.getEmail(), "Has sido removido del torneo: " + torneo.getNombre());
    }

    private void simularNotificacion(String email, String mensaje) {
        System.out.println("[NOTIFICACIÓN SIMULADA] Para: " + email + " - Mensaje: " + mensaje);
    }
}
