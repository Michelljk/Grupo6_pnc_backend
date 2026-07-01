package com.example.torneos.controller;

import com.example.torneos.dto.GeneralResponse;
import com.example.torneos.dto.request.PuntoJugadorDTO;
import com.example.torneos.dto.request.RankingDTO;
import com.example.torneos.service.PuntoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/puntos")
@RequiredArgsConstructor
public class PuntoController {

    private final PuntoService puntoService;

    @PostMapping
    public ResponseEntity<GeneralResponse> registrar(@Valid @RequestBody PuntoJugadorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(GeneralResponse.builder()
                .data(puntoService.registrarPuntos(dto))
                .message("Puntos registrados")
                .build());
    }

    @GetMapping("/jugador/{usuarioId}")
    public ResponseEntity<GeneralResponse> listarPorJugador(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(puntoService.obtenerPorJugador(usuarioId))
                .message("Listado de puntos por jugador")
                .build());
    }

    @GetMapping("/jugador/{usuarioId}/total")
    public ResponseEntity<GeneralResponse> totalPorJugador(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(puntoService.obtenerTotalPuntos(usuarioId))
                .message("Total de puntos del jugador")
                .build());
    }

    @GetMapping("/torneo/{torneoId}")
    public ResponseEntity<GeneralResponse> listarPorTorneo(@PathVariable Long torneoId) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(puntoService.obtenerPorTorneo(torneoId))
                .message("Listado de puntos por torneo")
                .build());
    }

    @GetMapping("/ranking")
    public ResponseEntity<GeneralResponse> ranking() {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(puntoService.obtenerRankingGlobal())
                .message("Ranking global de jugadores")
                .build());
    }
}
