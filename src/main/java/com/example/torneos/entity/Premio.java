package com.example.torneos.entity;

import com.example.torneos.enums.TipoPremio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "premios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Premio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPremio tipo;

    @Column(name = "valor_creditos")
    private Integer valorCreditos;

    @Column(name = "valor_monetario", precision = 10, scale = 2)
    private BigDecimal valorMonetario;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
    }
}
