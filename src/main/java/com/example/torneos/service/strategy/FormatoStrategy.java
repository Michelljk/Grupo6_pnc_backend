package com.example.torneos.service.strategy;

import com.example.torneos.entity.Torneo;

public interface FormatoStrategy {
    void validarConfiguracion(Torneo torneo);
}