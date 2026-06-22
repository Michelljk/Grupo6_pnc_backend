package com.example.torneos.controller;

import com.example.torneos.dto.AsignarGanadorRequest;
import com.example.torneos.dto.PremioDTO;
import com.example.torneos.dto.PremioTorneoDTO;
import com.example.torneos.enums.TipoPremio;
import com.example.torneos.service.PremioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/premios")
@RequiredArgsConstructor
public class PremioController {

    private final PremioService premioService;

    @GetMapping
    public ResponseEntity<List<PremioDTO>> listar(@RequestParam(required = false) TipoPremio tipo) {
        if (tipo != null) {
            return ResponseEntity.ok(premioService.buscarPorTipo(tipo));
        }
        return ResponseEntity.ok(premioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PremioDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(premioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PremioDTO> crear(@Valid @RequestBody PremioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(premioService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PremioDTO> actualizar(@PathVariable Long id, @Valid @RequestBody PremioDTO dto) {
        return ResponseEntity.ok(premioService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        premioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/asignar-torneo")
    public ResponseEntity<PremioTorneoDTO> asignarATorneo(@Valid @RequestBody PremioTorneoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(premioService.asignarATorneo(dto));
    }

    @GetMapping("/torneo/{torneoId}")
    public ResponseEntity<List<PremioTorneoDTO>> listarPorTorneo(@PathVariable Long torneoId) {
        return ResponseEntity.ok(premioService.listarPorTorneo(torneoId));
    }

    @PostMapping("/asignar-ganador")
    public ResponseEntity<PremioTorneoDTO> asignarGanador(@Valid @RequestBody AsignarGanadorRequest request) {
        return ResponseEntity.ok(premioService.asignarGanador(request));
    }
}
