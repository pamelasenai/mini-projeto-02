package com.senai.miniprojeto02.controllers.dto.response;

import com.senai.miniprojeto02.entities.ProfessorEntity;

public record DisciplinaResponse(
        Long id,
        String nome,
        ProfessorEntity professor
) {
}
