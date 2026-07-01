package com.example.torneos.controller;

import com.example.torneos.dto.GeneralResponse;
import com.example.torneos.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/populares")
    public ResponseEntity<GeneralResponse> getPopulares() {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(reporteService.obtenerTorneosMasPopulares())
                .message("Torneos más populares")
                .build());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/estadisticas")
    public ResponseEntity<GeneralResponse> getEstadisticas() {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(reporteService.obtenerEstadisticasGenerales())
                .message("Estadísticas generales del sistema")
                .build());
    }
}
