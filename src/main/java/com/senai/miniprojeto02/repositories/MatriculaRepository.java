package com.senai.miniprojeto02.repositories;

import com.senai.miniprojeto02.entities.MatriculaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatriculaRepository extends JpaRepository<MatriculaEntity, Long> {
    List<MatriculaEntity> findByAlunoId(Long alunoId);
    List<MatriculaEntity> findByDisciplinaId(Long disciplinaId);
}
