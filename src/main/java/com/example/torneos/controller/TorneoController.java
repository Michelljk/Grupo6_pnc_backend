package com.example.torneos.controller;

import com.example.torneos.dto.GeneralResponse;
import com.example.torneos.dto.torneo.TorneoRequest;
import com.example.torneos.service.TorneoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/torneos")
@RequiredArgsConstructor
public class TorneoController {

    private final TorneoService torneoService;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<GeneralResponse> crearTorneo(@Valid @RequestBody TorneoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(GeneralResponse.builder()
                .data(torneoService.crearTorneo(request))
                .message("Torneo creado exitosamente")
                .build());
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> listarTorneos() {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(torneoService.listarTorneos())
                .message("Listado de torneos")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(torneoService.obtenerPorId(id))
                .message("Torneo encontrado")
                .build());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> actualizarTorneo(@PathVariable Long id, @Valid @RequestBody TorneoRequest request) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(torneoService.actualizarTorneo(id, request))
                .message("Torneo actualizado exitosamente")
                .build());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> eliminarTorneo(@PathVariable Long id) {
        torneoService.eliminarTorneo(id);
        return ResponseEntity.ok(GeneralResponse.builder()
                .message("Torneo eliminado exitosamente")
                .build());
    }

    @PostMapping("/{id}/inscribir/{usuarioId}")
    public ResponseEntity<GeneralResponse> inscribirUsuario(@PathVariable Long id, @PathVariable Long usuarioId) {
        torneoService.inscribirUsuario(id, usuarioId);
        return ResponseEntity.ok(GeneralResponse.builder()
                .message("Usuario inscrito exitosamente")
                .build());
    }

    @DeleteMapping("/{id}/remover/{usuarioId}")
    public ResponseEntity<GeneralResponse> removerUsuario(@PathVariable Long id, @PathVariable Long usuarioId) {
        torneoService.removerUsuario(id, usuarioId);
        return ResponseEntity.ok(GeneralResponse.builder()
                .message("Usuario removido del torneo")
                .build());
    }
}
