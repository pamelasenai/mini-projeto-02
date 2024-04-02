package com.senai.miniprojeto02.repositories;

import com.senai.miniprojeto02.entities.AlunoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<AlunoEntity, Long> {
}
