package com.example.torneos.controller;

import com.example.torneos.dto.GeneralResponse;
import com.example.torneos.service.TorneoEstadisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estadisticas")
@RequiredArgsConstructor
public class EstadisticasController {

    private final TorneoEstadisticasService estadisticasService;

    @GetMapping("/jugador/{usuarioId}")
    public ResponseEntity<GeneralResponse> obtenerEstadisticasJugador(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(estadisticasService.obtenerEstadisticasJugador(usuarioId))
                .message("Estadísticas del jugador obtenidas exitosamente")
                .build());
    }

    @GetMapping("/jugador/{usuarioId}/torneo/{torneoId}")
    public ResponseEntity<GeneralResponse> obtenerEstadisticasJugadorEnTorneo(
            @PathVariable Long usuarioId,
            @PathVariable Long torneoId) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(estadisticasService.obtenerEstadisticasJugadorEnTorneo(usuarioId, torneoId))
                .message("Estadísticas del jugador en el torneo obtenidas exitosamente")
                .build());
    }

    @GetMapping("/jugador/{usuarioId}/historial")
    public ResponseEntity<GeneralResponse> obtenerHistorialTorneosJugador(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(estadisticasService.obtenerHistorialTorneosJugador(usuarioId))
                .message("Historial de torneos del jugador obtenido exitosamente")
                .build());
    }
}
