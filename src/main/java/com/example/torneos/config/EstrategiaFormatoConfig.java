package com.example.torneos.config;

import com.example.torneos.enums.FormatoTorneo;
import com.example.torneos.service.strategy.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class EstrategiaFormatoConfig {

    @Bean
    public Map<FormatoTorneo, FormatoStrategy> formatoStrategies(
            EliminatoriaSimpleStrategy simple,
            EliminatoriaDobleStrategy doble,
            LigaStrategy liga,
            RoundRobinStrategy roundRobin,
            SuizoStrategy suizo) {

        Map<FormatoTorneo, FormatoStrategy> strategies = new HashMap<>();
        strategies.put(FormatoTorneo.ELIMINACION_SIMPLE, simple);
        strategies.put(FormatoTorneo.ELIMINACION_DOBLE, doble);
        strategies.put(FormatoTorneo.LIGA, liga);
        strategies.put(FormatoTorneo.ROUND_ROBIN, roundRobin);
        strategies.put(FormatoTorneo.SUIZO, suizo);
        return strategies;
    }
}