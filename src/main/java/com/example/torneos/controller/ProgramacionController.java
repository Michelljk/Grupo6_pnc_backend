package com.example.torneos.controller;

import com.example.torneos.dto.GeneralResponse;
import com.example.torneos.service.ProgramacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/programacion")
@RequiredArgsConstructor
public class ProgramacionController {

    private final ProgramacionService programacionService;

    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('ORGANIZADOR')")
    @PostMapping("/enfrentamiento/{enfrentamientoId}")
    public ResponseEntity<GeneralResponse> programarEnfrentamiento(
            @PathVariable Long enfrentamientoId,
            @RequestParam LocalDateTime fechaHora) {
        programacionService.programarEnfrentamiento(enfrentamientoId, fechaHora);
        return ResponseEntity.ok(GeneralResponse.builder()
                .message("Enfrentamiento programado exitosamente")
                .build());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('ORGANIZADOR')")
    @PostMapping("/bracket/{bracketId}")
    public ResponseEntity<GeneralResponse> programarBracketCompleto(
            @PathVariable Long bracketId,
            @RequestParam LocalDateTime fechaInicio,
            @RequestParam(defaultValue = "60") int minutosEntreProgramas) {
        programacionService.programarBracketCompleto(bracketId, fechaInicio, minutosEntreProgramas);
        return ResponseEntity.ok(GeneralResponse.builder()
                .message("Bracket programado exitosamente")
                .build());
    }

    @GetMapping("/proximos/{usuarioId}")
    public ResponseEntity<GeneralResponse> obtenerEnfrentamientosProximos(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "7") int diasAdelante) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(programacionService.obtenerEnfrentamientosProximos(usuarioId, diasAdelante))
                .message("Enfrentamientos próximos obtenidos exitosamente")
                .build());
    }

    @GetMapping("/hoy/{usuarioId}")
    public ResponseEntity<GeneralResponse> obtenerEnfrentamientosDelDia(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(programacionService.obtenerEnfrentamientosDelDia(usuarioId))
                .message("Enfrentamientos del día obtenidos exitosamente")
                .build());
    }
}
