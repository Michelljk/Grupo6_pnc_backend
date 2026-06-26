package com.example.torneos.controller;

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
    public ResponseEntity<PuntoJugadorDTO> registrar(@Valid @RequestBody PuntoJugadorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(puntoService.registrarPuntos(dto));
    }

    @GetMapping("/jugador/{usuarioId}")
    public ResponseEntity<List<PuntoJugadorDTO>> listarPorJugador(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(puntoService.obtenerPorJugador(usuarioId));
    }

    @GetMapping("/jugador/{usuarioId}/total")
    public ResponseEntity<Integer> totalPorJugador(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(puntoService.obtenerTotalPuntos(usuarioId));
    }

    @GetMapping("/torneo/{torneoId}")
    public ResponseEntity<List<PuntoJugadorDTO>> listarPorTorneo(@PathVariable Long torneoId) {
        return ResponseEntity.ok(puntoService.obtenerPorTorneo(torneoId));
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<RankingDTO>> ranking() {
        return ResponseEntity.ok(puntoService.obtenerRankingGlobal());
    }
}
