package com.example.torneos.controller;

import com.example.torneos.dto.request.ComprarCreditosRequest;
import com.example.torneos.dto.request.CrearPagoRequest;
import com.example.torneos.dto.response.PagoResponse;
import com.example.torneos.dto.GeneralResponse;
import com.example.torneos.entity.WebhookStripe;
import com.example.torneos.repository.WebhookStripeRepository;
import com.example.torneos.service.PagoService;
import com.example.torneos.service.StripeService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;
    private final StripeService stripeService;
    private final WebhookStripeRepository webhookStripeRepository;

    @PostMapping("/crear-intento")
    public ResponseEntity<GeneralResponse> crearIntento(@Valid @RequestBody CrearPagoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(GeneralResponse.builder()
                .data(pagoService.crearIntentoPago(request))
                .message("Intento de pago creado")
                .build());
    }

    @PostMapping("/comprar-creditos")
    public ResponseEntity<GeneralResponse> comprarCreditos(@Valid @RequestBody ComprarCreditosRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(GeneralResponse.builder()
                .data(pagoService.crearPagoCreditos(request))
                .message("Compra de créditos iniciada")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(pagoService.buscarPorId(id))
                .message("Pago encontrado")
                .build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<GeneralResponse> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(pagoService.listarPorUsuario(usuarioId))
                .message("Listado de pagos del usuario")
                .build());
    }

    @GetMapping("/verificar-acceso")
    public ResponseEntity<GeneralResponse> verificarAcceso(@RequestParam Long usuarioId, @RequestParam Long torneoId) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(pagoService.tienePagoCompletado(usuarioId, torneoId))
                .message("Estado de acceso verificado")
                .build());
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;
        try {
            event = stripeService.construirEvento(payload, sigHeader);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Firma invalida");
        }

        if (webhookStripeRepository.existsByStripeEventId(event.getId())) {
            return ResponseEntity.ok("Evento ya procesado");
        }

        WebhookStripe registro = new WebhookStripe();
        registro.setStripeEventId(event.getId());
        registro.setTipoEvento(event.getType());
        registro.setPayload(payload);
        registro.setProcesado(false);
        webhookStripeRepository.save(registro);

        Optional<StripeObject> stripeObject = event.getDataObjectDeserializer().getObject();

        switch (event.getType()) {
            case "payment_intent.succeeded" -> stripeObject.ifPresent(obj -> {
                pagoService.procesarPagoExitoso(((PaymentIntent) obj).getId());
                registro.setProcesado(true);
                webhookStripeRepository.save(registro);
            });
            case "payment_intent.payment_failed" -> stripeObject.ifPresent(obj -> {
                pagoService.procesarPagoFallido(((PaymentIntent) obj).getId());
                registro.setProcesado(true);
                webhookStripeRepository.save(registro);
            });
        }

        return ResponseEntity.ok("Webhook procesado");
    }
}
