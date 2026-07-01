package com.example.torneos.controller;

import com.example.torneos.dto.GeneralResponse;
import com.example.torneos.dto.request.CrearDisputaRequest;
import com.example.torneos.dto.request.ResolverDisputaRequest;
import com.example.torneos.mapper.DisputaMapper;
import com.example.torneos.service.DisputaService;

import java.util.stream.Collectors;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/disputas")
@RequiredArgsConstructor
public class DisputaController {

    private final DisputaService disputaService;

    @PostMapping("/reportar/{resultadoId}")
    public ResponseEntity<GeneralResponse> crearDisputa(
            @PathVariable Long resultadoId,
            @Valid @RequestBody CrearDisputaRequest request) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(DisputaMapper.toResponse(disputaService.crearDisputa(resultadoId, request)))
                .message("Disputa reportada exitosamente")
                .build());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/resolver/{disputaId}")
    public ResponseEntity<GeneralResponse> resolverDisputa(
            @PathVariable Long disputaId,
            @Valid @RequestBody ResolverDisputaRequest request) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(DisputaMapper.toResponse(disputaService.resolverDisputa(disputaId, request)))
                .message("Disputa resuelta")
                .build());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/pendientes")
    public ResponseEntity<GeneralResponse> listarPendientes() {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(disputaService.listarPendientes().stream().map(DisputaMapper::toResponse).collect(Collectors.toList()))
                .message("Listado de disputas pendientes")
                .build());
    }
}
