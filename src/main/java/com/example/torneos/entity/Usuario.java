package com.example.torneos.entity;

import com.example.torneos.enums.RolUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolUsuario rol;

    @Column(name = "creditos_virtuales")
    private Integer creditosVirtuales = 0;

    @Column(name = "elo_rating")
    private Integer eloRating = 1000;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
        if (creditosVirtuales == null) creditosVirtuales = 0;
        if (eloRating == null) eloRating = 1000;
    }
}
