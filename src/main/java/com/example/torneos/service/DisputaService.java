package com.example.torneos.service;

import com.example.torneos.dto.request.CrearDisputaRequest;
import com.example.torneos.dto.request.ResolverDisputaRequest;
import com.example.torneos.entity.Disputa;
import com.example.torneos.entity.Resultado;
import com.example.torneos.entity.Usuario;
import com.example.torneos.enums.EstadoDisputa;
import com.example.torneos.exception.ResourceNotFoundException;
import com.example.torneos.repository.DisputaRepository;
import com.example.torneos.repository.ResultadoRepository;
import com.example.torneos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisputaService {

    private final DisputaRepository disputaRepository;
    private final ResultadoRepository resultadoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Disputa crearDisputa(Long resultadoId, CrearDisputaRequest request) {
        Resultado resultado = resultadoRepository.findById(resultadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Resultado no encontrado"));

        Usuario usuario = usuarioRepository.findById(request.getUsuarioReportaId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Disputa disputa = Disputa.builder()
                .resultado(resultado)
                .usuarioReporta(usuario)
                .descripcion(request.getDescripcion())
                .estado(EstadoDisputa.PENDIENTE)
                .build();

        return disputaRepository.save(disputa);
    }

    @Transactional
    public Disputa resolverDisputa(Long disputaId, ResolverDisputaRequest request) {
        Disputa disputa = disputaRepository.findById(disputaId)
                .orElseThrow(() -> new ResourceNotFoundException("Disputa no encontrada"));

        Usuario admin = usuarioRepository.findById(request.getAdministradorResuelveId())
                .orElseThrow(() -> new ResourceNotFoundException("Administrador no encontrado"));

        disputa.setAdministradorResuelve(admin);
        disputa.setResolucion(request.getResolucion());
        disputa.setEstado(request.getNuevoEstado());

        // Si la disputa cambia el resultado, aquí se podría implementar lógica adicional
        return disputaRepository.save(disputa);
    }

    @Transactional(readOnly = true)
    public List<Disputa> listarPendientes() {
        return disputaRepository.findByEstado(EstadoDisputa.PENDIENTE);
    }
}
