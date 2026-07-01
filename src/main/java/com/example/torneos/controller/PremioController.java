package com.example.torneos.controller;

import com.example.torneos.dto.GeneralResponse;
import com.example.torneos.dto.request.AsignarGanadorRequest;
import com.example.torneos.dto.request.PremioDTO;
import com.example.torneos.dto.request.PremioTorneoDTO;
import com.example.torneos.enums.TipoPremio;
import com.example.torneos.service.PremioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/premios")
@RequiredArgsConstructor
public class PremioController {

    private final PremioService premioService;

    @GetMapping
    public ResponseEntity<GeneralResponse> listar(@RequestParam(required = false) TipoPremio tipo) {
        Object data = (tipo != null) ? premioService.buscarPorTipo(tipo) : premioService.listarTodos();
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(data)
                .message("Listado de premios")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(premioService.buscarPorId(id))
                .message("Premio encontrado")
                .build());
    }

    @PostMapping
    public ResponseEntity<GeneralResponse> crear(@Valid @RequestBody PremioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(GeneralResponse.builder()
                .data(premioService.crear(dto))
                .message("Premio creado")
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> actualizar(@PathVariable Long id, @Valid @RequestBody PremioDTO dto) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(premioService.actualizar(id, dto))
                .message("Premio actualizado")
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> eliminar(@PathVariable Long id) {
        premioService.eliminar(id);
        return ResponseEntity.ok(GeneralResponse.builder()
                .message("Premio eliminado")
                .build());
    }

    @PostMapping("/asignar-torneo")
    public ResponseEntity<GeneralResponse> asignarATorneo(@Valid @RequestBody PremioTorneoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(GeneralResponse.builder()
                .data(premioService.asignarATorneo(dto))
                .message("Premio asignado al torneo")
                .build());
    }

    @GetMapping("/torneo/{torneoId}")
    public ResponseEntity<GeneralResponse> listarPorTorneo(@PathVariable Long torneoId) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(premioService.listarPorTorneo(torneoId))
                .message("Premios del torneo")
                .build());
    }

    @PostMapping("/asignar-ganador")
    public ResponseEntity<GeneralResponse> asignarGanador(@Valid @RequestBody AsignarGanadorRequest request) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(premioService.asignarGanador(request))
                .message("Ganador de premio asignado")
                .build());
    }
}
