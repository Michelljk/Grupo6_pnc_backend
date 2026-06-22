package com.example.torneos.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "stripe")
public class StripeProperties {
    private String apiKey;
    private String webhookSecret;
}
