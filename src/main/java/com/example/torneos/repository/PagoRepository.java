package com.example.torneos.repository;

import com.example.torneos.entity.Pago;
import com.example.torneos.enums.EstadoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByUsuarioId(Long usuarioId);
    List<Pago> findByTorneoId(Long torneoId);
    Optional<Pago> findByStripePaymentIntentId(String paymentIntentId);
    boolean existsByUsuarioIdAndTorneoIdAndEstado(Long usuarioId, Long torneoId, EstadoPago estado);
}
