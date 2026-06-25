package com.example.torneos.service;

import com.example.torneos.dto.TorneoRequest;
import com.example.torneos.entity.Torneo;
import com.example.torneos.entity.Usuario;
import com.example.torneos.enums.FormatoTorneo;
import com.example.torneos.repository.TorneoRepository;
import com.example.torneos.repository.UsuarioRepository;
import com.example.torneos.service.strategy.FormatoStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class TorneoService {

    private final TorneoRepository torneoRepository;
    private final UsuarioRepository usuarioRepository;
    private final Map<FormatoTorneo, FormatoStrategy> formatStrategies;

    public TorneoService(TorneoRepository torneoRepository,
                         UsuarioRepository usuarioRepository,
                         Map<FormatoTorneo, FormatoStrategy> formatStrategies) {
        this.torneoRepository = torneoRepository;
        this.usuarioRepository = usuarioRepository;
        this.formatStrategies = formatStrategies;
    }

    @Transactional
    public Torneo crearTorneo(TorneoRequest dto) {
        if (dto.getEloMinimo() > dto.getEloMaximo()) {
            throw new IllegalArgumentException("El ELO mínimo no puede ser mayor que el ELO máximo");
        }

        Torneo torneo = new Torneo();
        torneo.setNombre(dto.getNombre());
        torneo.setJuego(dto.getJuego());
        torneo.setFormato(dto.getFormato());
        torneo.setMaxParticipantes(dto.getMaxParticipantes());
        torneo.setEloMinimo(dto.getEloMinimo());
        torneo.setEloMaximo(dto.getEloMaximo());
        torneo.setEsPremium(dto.getEsPremium());
        torneo.setCostoInscripcion(dto.getCostoInscripcion());

        FormatoStrategy strategy = formatStrategies.get(torneo.getFormato());
        if (strategy != null) {
            strategy.validarConfiguracion(torneo);
        }

        return torneoRepository.save(torneo);
    }

    @Transactional
    public void inscribirUsuario(Long torneoId, Long usuarioId) {
        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new IllegalArgumentException("Torneo no encontrado"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (torneo.getParticipantes().size() >= torneo.getMaxParticipantes()) {
            throw new IllegalArgumentException("El torneo ha alcanzado el límite máximo de participantes");
        }

        if (usuario.getEloRating() < torneo.getEloMinimo() || usuario.getEloRating() > torneo.getEloMaximo()) {
            throw new IllegalArgumentException("El rango ELO del usuario no es válido para este torneo");
        }

        torneo.getParticipantes().add(usuario);
        torneoRepository.save(torneo);
        simularNotificacionPush(usuario.getEmail(), "Inscripción exitosa al torneo " + torneo.getNombre());
    }

    @Transactional
    public void removerUsuario(Long torneoId, Long usuarioId) {
        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new IllegalArgumentException("Torneo no encontrado"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        torneo.getParticipantes().remove(usuario);
        torneoRepository.save(torneo);
    }

    private void simularNotificacionPush(String email, String mensaje) {
        System.out.println("Notification sent to " + email + ": " + mensaje);
    }
}