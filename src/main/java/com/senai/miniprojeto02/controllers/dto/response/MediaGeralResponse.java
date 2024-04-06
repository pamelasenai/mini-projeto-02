package com.senai.miniprojeto02.controllers.dto.response;

import com.senai.miniprojeto02.entities.DisciplinaEntity;

public record MediaGeralResponse(
        double mediaGeral,
        DisciplinaEntity disciplina
) {
}
