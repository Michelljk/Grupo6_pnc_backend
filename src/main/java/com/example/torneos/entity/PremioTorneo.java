package com.example.torneos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "premios_torneos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PremioTorneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id", nullable = false)
    private Torneo torneo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "premio_id", nullable = false)
    private Premio premio;

    @Column(nullable = false)
    private Integer posicion;

    private Boolean asignado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_ganador_id")
    private Usuario usuarioGanador;

    @Column(name = "asignado_en")
    private LocalDateTime asignadoEn;
}
