package com.example.torneos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "puntos_jugador")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PuntoJugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Integer puntos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id")
    private Torneo torneoOrigen;

    private String descripcion;

    @Column(name = "asignado_en")
    private LocalDateTime asignadoEn;

    @PrePersist
    protected void onCreate() {
        asignadoEn = LocalDateTime.now();
    }
}
