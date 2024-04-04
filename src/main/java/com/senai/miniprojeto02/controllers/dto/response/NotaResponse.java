package com.senai.miniprojeto02.controllers.dto.response;

import com.senai.miniprojeto02.entities.MatriculaEntity;
import com.senai.miniprojeto02.entities.ProfessorEntity;

public record NotaResponse(
        Long id,
        Double nota,
        Double coeficiente,
        MatriculaEntity matricula,
        ProfessorEntity professor
) {
}
