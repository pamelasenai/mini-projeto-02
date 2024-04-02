package com.senai.miniprojeto02.controllers.dto.request;

import java.time.LocalDate;

public record AlunoRequest(
        String nome,
        LocalDate nascimento
) {
}
