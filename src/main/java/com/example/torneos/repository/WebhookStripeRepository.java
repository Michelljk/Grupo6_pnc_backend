package com.example.torneos.repository;

import com.example.torneos.entity.WebhookStripe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebhookStripeRepository extends JpaRepository<WebhookStripe, Long> {
    boolean existsByStripeEventId(String stripeEventId);
}
