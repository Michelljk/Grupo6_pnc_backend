package com.example.torneos.controller;

import com.example.torneos.dto.request.AsignarGanadorRequest;
import com.example.torneos.entity.Torneo;
import com.example.torneos.service.TorneoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/torneos")
public class TorneoController {

    private final TorneoService torneoService;

    public TorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    @PostMapping
    public ResponseEntity<Torneo> crearTorneo(@Valid @RequestBody AsignarGanadorRequest.TorneoRequest request) {
        return ResponseEntity.ok(torneoService.crearTorneo(request));
    }

    @PostMapping("/{id}/usuarios/{usuarioId}")
    public ResponseEntity<Void> inscribirUsuario(@PathVariable Long id, @PathVariable Long usuarioId) {
        torneoService.inscribirUsuario(id, usuarioId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/usuarios/{usuarioId}")
    public ResponseEntity<Void> removerUsuario(@PathVariable Long id, @PathVariable Long usuarioId) {
        torneoService.removerUsuario(id, usuarioId);
        return ResponseEntity.ok().build();
    }
}