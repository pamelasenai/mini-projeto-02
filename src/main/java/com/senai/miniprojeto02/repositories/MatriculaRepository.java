package com.senai.miniprojeto02.repositories;

import com.senai.miniprojeto02.entities.MatriculaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatriculaRepository extends JpaRepository<MatriculaEntity, Long> {
    List<MatriculaEntity> findByAlunoId(Long alunoId);
    List<MatriculaEntity> findByDisciplinaId(Long disciplinaId);

    @Query(value = "SELECT m.disciplina_id AS disciplina, AVG(m.media_final) AS media " +
            "FROM matricula m " +
            "WHERE m.aluno_id = :alunoId " +
            "GROUP BY m.disciplina_id",
            nativeQuery = true
    )
    List<Object[]> findMediaGeralAlunoId(
            @Param("alunoId") Long alunoId
    );
}
