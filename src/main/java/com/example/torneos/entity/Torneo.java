package com.example.torneos.entity;

import com.example.torneos.enums.EstadoTorneo;
import com.example.torneos.enums.FormatoTorneo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "elo_minimo")
    private Integer eloMinimo;

    @Column(name = "elo_maximo")
    private Integer eloMaximo;

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

    @ManyToMany
    @JoinTable(
            name = "torneo_participantes",
            joinColumns = @JoinColumn(name = "torneo_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> participantes;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
        if (estado == null) estado = EstadoTorneo.PROXIMO;
        if (esPremium == null) esPremium = false;
        if (eloMinimo == null) eloMinimo = 0;
        if (eloMaximo == null) eloMaximo = 3000;
    }
}