package com.senai.miniprojeto02.controllers.dto.response;

import com.senai.miniprojeto02.entities.DisciplinaEntity;
import com.senai.miniprojeto02.entities.ProfessorEntity;

public record NotaResponse(
        Long id,
        Double nota,
        Double coeficiente,
        DisciplinaEntity disciplina,
        ProfessorEntity professor
) {
}
