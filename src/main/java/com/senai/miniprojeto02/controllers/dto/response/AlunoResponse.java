package com.senai.miniprojeto02.controllers.dto.response;

import java.time.LocalDate;

public record AlunoResponse(
        Long id,
        String nome,
        LocalDate nascimento
) {
}
