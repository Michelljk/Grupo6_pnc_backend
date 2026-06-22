package com.example.torneos.entity;

import com.example.torneos.enums.EstadoTorneo;
import com.example.torneos.enums.FormatoTorneo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "torneos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String juego;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormatoTorneo formato;

    @Column(name = "max_participantes")
    private Integer maxParticipantes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTorneo estado;

    @Column(name = "es_premium")
    private Boolean esPremium = false;

    @Column(name = "costo_inscripcion", precision = 10, scale = 2)
    private BigDecimal costoInscripcion;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
        if (estado == null) estado = EstadoTorneo.PROXIMO;
        if (esPremium == null) esPremium = false;
    }
}
