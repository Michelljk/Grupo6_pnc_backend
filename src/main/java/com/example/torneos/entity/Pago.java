package com.example.torneos.entity;

import com.example.torneos.enums.ConceptoPago;
import com.example.torneos.enums.EstadoPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false, length = 3)
    private String moneda;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConceptoPago concepto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id")
    private Torneo torneo;

    @Column(name = "creditos_comprados")
    private Integer creditosComprados;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estado;

    @Column(name = "stripe_payment_intent_id", unique = true)
    private String stripePaymentIntentId;

    @Column(name = "stripe_client_secret")
    private String stripeClientSecret;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
        actualizadoEn = LocalDateTime.now();
        if (estado == null) estado = EstadoPago.PENDIENTE;
    }

    @PreUpdate
    protected void onUpdate() {
        actualizadoEn = LocalDateTime.now();
    }
}
