package com.example.torneos.service;

import com.example.torneos.dto.request.ComprarCreditosRequest;
import com.example.torneos.dto.request.CrearPagoRequest;
import com.example.torneos.dto.response.PagoResponse;
import com.example.torneos.entity.Pago;
import com.example.torneos.entity.Torneo;
import com.example.torneos.entity.Usuario;
import com.example.torneos.enums.ConceptoPago;
import com.example.torneos.enums.EstadoPago;
import com.example.torneos.exception.ResourceNotFoundException;
import com.example.torneos.repository.PagoRepository;
import com.example.torneos.repository.TorneoRepository;
import com.example.torneos.repository.UsuarioRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagoService {

    private static final BigDecimal PRECIO_POR_CREDITO = new BigDecimal("0.10");

    private final PagoRepository pagoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TorneoRepository torneoRepository;
    private final StripeService stripeService;

    @Transactional
    public PagoResponse crearIntentoPago(CrearPagoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + request.getUsuarioId()));

        Torneo torneo = null;
        if (request.getTorneoId() != null) {
            torneo = torneoRepository.findById(request.getTorneoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Torneo no encontrado: " + request.getTorneoId()));

            if (request.getConcepto() == ConceptoPago.INSCRIPCION_TORNEO) {
                boolean yaPago = pagoRepository.existsByUsuarioIdAndTorneoIdAndEstado(
                        request.getUsuarioId(), request.getTorneoId(), EstadoPago.COMPLETADO);
                if (yaPago) {
                    throw new IllegalStateException("El usuario ya tiene un pago completado para este torneo");
                }
            }
        }

        String descripcion = request.getConcepto() == ConceptoPago.INSCRIPCION_TORNEO
                ? "Inscripcion torneo " + (torneo != null ? torneo.getNombre() : "")
                : "Compra de creditos virtuales";

        PaymentIntent paymentIntent;
        try {
            paymentIntent = stripeService.crearPaymentIntent(request.getMonto(), request.getMoneda(), descripcion);
        } catch (StripeException e) {
            throw new IllegalStateException("Error al crear el intento de pago: " + e.getMessage());
        }

        Pago pago = new Pago();
        pago.setUsuario(usuario);
        pago.setMonto(request.getMonto());
        pago.setMoneda(request.getMoneda());
        pago.setConcepto(request.getConcepto());
        pago.setTorneo(torneo);
        pago.setCreditosComprados(request.getCreditosComprados());
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setStripePaymentIntentId(paymentIntent.getId());
        pago.setStripeClientSecret(paymentIntent.getClientSecret());

        return toResponse(pagoRepository.save(pago));
    }

    @Transactional
    public PagoResponse crearPagoCreditos(ComprarCreditosRequest request) {
        BigDecimal monto = PRECIO_POR_CREDITO.multiply(BigDecimal.valueOf(request.getCantidadCreditos()));

        CrearPagoRequest pagoRequest = new CrearPagoRequest();
        pagoRequest.setUsuarioId(request.getUsuarioId());
        pagoRequest.setMonto(monto);
        pagoRequest.setMoneda("usd");
        pagoRequest.setConcepto(ConceptoPago.COMPRA_CREDITOS);
        pagoRequest.setCreditosComprados(request.getCantidadCreditos());

        return crearIntentoPago(pagoRequest);
    }

    @Transactional
    public void procesarPagoExitoso(String paymentIntentId) {
        Pago pago = pagoRepository.findByStripePaymentIntentId(paymentIntentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pago no encontrado para PaymentIntent: " + paymentIntentId));

        if (pago.getEstado() == EstadoPago.COMPLETADO) {
            return;
        }

        pago.setEstado(EstadoPago.COMPLETADO);

        if (pago.getConcepto() == ConceptoPago.COMPRA_CREDITOS && pago.getCreditosComprados() != null) {
            Usuario usuario = pago.getUsuario();
            usuario.setCreditosVirtuales(usuario.getCreditosVirtuales() + pago.getCreditosComprados());
            usuarioRepository.save(usuario);
        }

        pagoRepository.save(pago);
    }

    @Transactional
    public void procesarPagoFallido(String paymentIntentId) {
        pagoRepository.findByStripePaymentIntentId(paymentIntentId).ifPresent(pago -> {
            pago.setEstado(EstadoPago.FALLIDO);
            pagoRepository.save(pago);
        });
    }

    @Transactional(readOnly = true)
    public PagoResponse buscarPorId(Long id) {
        return toResponse(pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado: " + id)));
    }

    @Transactional(readOnly = true)
    public List<PagoResponse> listarPorUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuario no encontrado: " + usuarioId);
        }
        return pagoRepository.findByUsuarioId(usuarioId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean tienePagoCompletado(Long usuarioId, Long torneoId) {
        return pagoRepository.existsByUsuarioIdAndTorneoIdAndEstado(usuarioId, torneoId, EstadoPago.COMPLETADO);
    }

    private PagoResponse toResponse(Pago pago) {
        PagoResponse response = new PagoResponse();
        response.setId(pago.getId());
        response.setUsuarioId(pago.getUsuario().getId());
        response.setMonto(pago.getMonto());
        response.setMoneda(pago.getMoneda());
        response.setConcepto(pago.getConcepto());
        response.setEstado(pago.getEstado());
        response.setStripePaymentIntentId(pago.getStripePaymentIntentId());
        response.setStripeClientSecret(pago.getStripeClientSecret());
        response.setCreadoEn(pago.getCreadoEn());
        response.setCreditosComprados(pago.getCreditosComprados());
        if (pago.getTorneo() != null) {
            response.setTorneoId(pago.getTorneo().getId());
        }
        return response;
    }
}
