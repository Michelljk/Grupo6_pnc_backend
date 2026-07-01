package com.example.torneos.service;

import com.example.torneos.config.StripeProperties;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class StripeService {

    private final StripeProperties stripeProperties;

    public PaymentIntent crearPaymentIntent(BigDecimal monto, String moneda, String descripcion) throws StripeException {
        long montoEnCentavos = monto.multiply(BigDecimal.valueOf(100)).longValue();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(montoEnCentavos)
                .setCurrency(moneda.toLowerCase())
                .setDescription(descripcion)
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .build();

        return PaymentIntent.create(params);
    }

    public Event construirEvento(String payload, String sigHeader) throws SignatureVerificationException {
        return Webhook.constructEvent(payload, sigHeader, stripeProperties.getWebhookSecret());
    }
}
