package com.example.torneos.dto.response;

import com.example.torneos.enums.ConceptoPago;
import com.example.torneos.enums.EstadoPago;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagoResponse {
    private Long id;
    private Long usuarioId;
    private BigDecimal monto;
    private String moneda;
    private ConceptoPago concepto;
    private Long torneoId;
    private Integer creditosComprados;
    private EstadoPago estado;
    private String stripePaymentIntentId;
    private String stripeClientSecret;
    private LocalDateTime creadoEn;
}
