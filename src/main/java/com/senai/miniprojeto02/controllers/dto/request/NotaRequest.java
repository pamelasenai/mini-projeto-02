package com.senai.miniprojeto02.controllers.dto.request;

public record NotaRequest(
        Long matriculaId,
        Double nota,
        Double coeficiente
) {
}
