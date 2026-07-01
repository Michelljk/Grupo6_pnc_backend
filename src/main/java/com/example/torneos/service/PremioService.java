package com.example.torneos.service;

import com.example.torneos.dto.request.AsignarGanadorRequest;
import com.example.torneos.dto.request.PremioDTO;
import com.example.torneos.dto.request.PremioTorneoDTO;
import com.example.torneos.entity.Premio;
import com.example.torneos.entity.PremioTorneo;
import com.example.torneos.entity.Torneo;
import com.example.torneos.entity.Usuario;
import com.example.torneos.enums.EstadoTorneo;
import com.example.torneos.enums.TipoPremio;
import com.example.torneos.exception.ResourceNotFoundException;
import com.example.torneos.repository.PremioRepository;
import com.example.torneos.repository.PremioTorneoRepository;
import com.example.torneos.repository.TorneoRepository;
import com.example.torneos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PremioService {

    private final PremioRepository premioRepository;
    private final PremioTorneoRepository premioTorneoRepository;
    private final TorneoRepository torneoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PuntoService puntoService;

    @Transactional(readOnly = true)
    public List<PremioDTO> listarTodos() {
        return premioRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PremioDTO buscarPorId(Long id) {
        return toDTO(findPremio(id));
    }

    @Transactional(readOnly = true)
    public List<PremioDTO> buscarPorTipo(TipoPremio tipo) {
        return premioRepository.findByTipo(tipo).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PremioDTO crear(PremioDTO dto) {
        Premio premio = new Premio();
        premio.setNombre(dto.getNombre());
        premio.setDescripcion(dto.getDescripcion());
        premio.setTipo(dto.getTipo());
        premio.setValorCreditos(dto.getValorCreditos());
        premio.setValorMonetario(dto.getValorMonetario());
        return toDTO(premioRepository.save(premio));
    }

    @Transactional
    public PremioDTO actualizar(Long id, PremioDTO dto) {
        Premio premio = findPremio(id);
        premio.setNombre(dto.getNombre());
        premio.setDescripcion(dto.getDescripcion());
        premio.setTipo(dto.getTipo());
        premio.setValorCreditos(dto.getValorCreditos());
        premio.setValorMonetario(dto.getValorMonetario());
        return toDTO(premioRepository.save(premio));
    }

    @Transactional
    public void eliminar(Long id) {
        findPremio(id);
        premioRepository.deleteById(id);
    }

    @Transactional
    public PremioTorneoDTO asignarATorneo(PremioTorneoDTO dto) {
        Torneo torneo = torneoRepository.findById(dto.getTorneoId())
                .orElseThrow(() -> new ResourceNotFoundException("Torneo no encontrado: " + dto.getTorneoId()));
        Premio premio = findPremio(dto.getPremioId());

        premioTorneoRepository.findByTorneoIdAndPosicion(dto.getTorneoId(), dto.getPosicion())
                .ifPresent(p -> {
                    throw new IllegalStateException(
                            "Ya existe un premio para la posicion " + dto.getPosicion() + " en este torneo");
                });

        PremioTorneo premioTorneo = new PremioTorneo();
        premioTorneo.setTorneo(torneo);
        premioTorneo.setPremio(premio);
        premioTorneo.setPosicion(dto.getPosicion());
        premioTorneo.setAsignado(false);

        return toPremioTorneoDTO(premioTorneoRepository.save(premioTorneo));
    }

    @Transactional(readOnly = true)
    public List<PremioTorneoDTO> listarPorTorneo(Long torneoId) {
        return premioTorneoRepository.findByTorneoIdOrderByPosicionAsc(torneoId).stream()
                .map(this::toPremioTorneoDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PremioTorneoDTO asignarGanador(AsignarGanadorRequest request) {
        Torneo torneo = torneoRepository.findById(request.getTorneoId())
                .orElseThrow(() -> new ResourceNotFoundException("Torneo no encontrado: " + request.getTorneoId()));

        if (torneo.getEstado() != EstadoTorneo.FINALIZADO) {
            throw new IllegalStateException("El torneo debe estar en estado FINALIZADO para asignar premios");
        }

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + request.getUsuarioId()));

        PremioTorneo premioTorneo = premioTorneoRepository
                .findByTorneoIdAndPosicion(request.getTorneoId(), request.getPosicion())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No hay premio configurado para la posicion " + request.getPosicion()));

        if (Boolean.TRUE.equals(premioTorneo.getAsignado())) {
            throw new IllegalStateException("El premio para la posicion " + request.getPosicion() + " ya fue asignado");
        }

        premioTorneo.setUsuarioGanador(usuario);
        premioTorneo.setAsignado(true);
        premioTorneo.setAsignadoEn(LocalDateTime.now());

        Premio premio = premioTorneo.getPremio();
        if (premio.getTipo() == TipoPremio.VIRTUAL && premio.getValorCreditos() != null) {
            usuario.setCreditosVirtuales(usuario.getCreditosVirtuales() + premio.getValorCreditos());
            usuarioRepository.save(usuario);
            puntoService.otorgarPuntos(
                    usuario.getId(),
                    premio.getValorCreditos(),
                    torneo.getId(),
                    "Premio posicion " + request.getPosicion() + " en torneo: " + torneo.getNombre()
            );
        }

        return toPremioTorneoDTO(premioTorneoRepository.save(premioTorneo));
    }

    private Premio findPremio(Long id) {
        return premioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Premio no encontrado: " + id));
    }

    private PremioDTO toDTO(Premio premio) {
        PremioDTO dto = new PremioDTO();
        dto.setId(premio.getId());
        dto.setNombre(premio.getNombre());
        dto.setDescripcion(premio.getDescripcion());
        dto.setTipo(premio.getTipo());
        dto.setValorCreditos(premio.getValorCreditos());
        dto.setValorMonetario(premio.getValorMonetario());
        return dto;
    }

    private PremioTorneoDTO toPremioTorneoDTO(PremioTorneo pt) {
        PremioTorneoDTO dto = new PremioTorneoDTO();
        dto.setId(pt.getId());
        dto.setTorneoId(pt.getTorneo().getId());
        dto.setPremioId(pt.getPremio().getId());
        dto.setPosicion(pt.getPosicion());
        dto.setAsignado(pt.getAsignado());
        dto.setNombrePremio(pt.getPremio().getNombre());
        dto.setNombreTorneo(pt.getTorneo().getNombre());
        if (pt.getUsuarioGanador() != null) {
            dto.setUsuarioGanadorId(pt.getUsuarioGanador().getId());
        }
        return dto;
    }
}
