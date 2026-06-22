package com.example.torneos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "webhooks_stripe")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebhookStripe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stripe_event_id", unique = true, nullable = false)
    private String stripeEventId;

    @Column(name = "tipo_evento", nullable = false)
    private String tipoEvento;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private Boolean procesado = false;

    @Column(name = "recibido_en")
    private LocalDateTime recibidoEn;

    @PrePersist
    protected void onCreate() {
        recibidoEn = LocalDateTime.now();
        if (procesado == null) procesado = false;
    }
}
