package com.example.torneos.controller;

import com.example.torneos.dto.ComprarCreditosRequest;
import com.example.torneos.dto.CrearPagoRequest;
import com.example.torneos.dto.PagoResponse;
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
    public ResponseEntity<PagoResponse> crearIntento(@Valid @RequestBody CrearPagoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.crearIntentoPago(request));
    }

    @PostMapping("/comprar-creditos")
    public ResponseEntity<PagoResponse> comprarCreditos(@Valid @RequestBody ComprarCreditosRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.crearPagoCreditos(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PagoResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(pagoService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/verificar-acceso")
    public ResponseEntity<Boolean> verificarAcceso(@RequestParam Long usuarioId, @RequestParam Long torneoId) {
        return ResponseEntity.ok(pagoService.tienePagoCompletado(usuarioId, torneoId));
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
