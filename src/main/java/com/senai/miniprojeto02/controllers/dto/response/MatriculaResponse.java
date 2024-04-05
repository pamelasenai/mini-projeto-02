package com.senai.miniprojeto02.controllers.dto.response;

import com.senai.miniprojeto02.entities.AlunoEntity;
import com.senai.miniprojeto02.entities.DisciplinaEntity;

import java.time.LocalDate;

public record MatriculaResponse(
        Long id,
        LocalDate dataMatricula,
        Double mediaFinal,
        AlunoEntity aluno,
        DisciplinaEntity disciplina
) {
}
