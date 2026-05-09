package com.ejemplo.demo.api.dto;

import java.math.BigDecimal;

public record ProductoResponse(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock,
        Boolean estado,
        Long categoriaId,
        String categoriaNombre
) {
}