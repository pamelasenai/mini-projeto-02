package com.senai.miniprojeto02.entities;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "disciplinas")
public class DisciplinaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_matricula", nullable = false, length = 150)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private ProfessorEntity professor;
}
