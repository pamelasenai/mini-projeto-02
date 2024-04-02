package com.senai.miniprojeto02.services;

import com.senai.miniprojeto02.controllers.dto.request.AlunoRequest;
import com.senai.miniprojeto02.controllers.dto.response.AlunoResponse;
import com.senai.miniprojeto02.entities.AlunoEntity;
import com.senai.miniprojeto02.exception.NotFoundException;
import com.senai.miniprojeto02.repositories.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlunoService {
    private final AlunoRepository alunoRepository;

    public List<AlunoResponse> buscarTodos() {
        List<AlunoEntity> alunos = alunoRepository.findAll();
        List<AlunoResponse> alunosResponse = new ArrayList<>();
        alunos.forEach(aluno -> alunosResponse.add(
                alunoResponse(aluno)
        ));
        return alunosResponse;
    }

    public AlunoResponse buscarPorId(Long id) {
        AlunoEntity aluno = alunoRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Aluno com id " + id + " não encontrado.")
        );

        return alunoResponse(aluno);
    }

    public AlunoResponse criar(AlunoRequest alunoRequest) throws Exception {
        if (
                alunoRequest.nome() == null ||
                alunoRequest.nome().isBlank() ||
                alunoRequest.nome().length() < 3
        ) {
           throw new Exception("Nome não pode estar em branco e deve ter ao menos 3 caracteres.");
        }
        AlunoEntity aluno = new AlunoEntity();
        aluno.setId(null);
        aluno.setNome(alunoRequest.nome());
        aluno.setNascimento(alunoRequest.nascimento());

        AlunoEntity alunoSalvo = alunoRepository.save(aluno);
        return alunoResponse(alunoSalvo);
    }

    public AlunoResponse atualizar(Long id, AlunoRequest alunoRequest) {
        buscarPorId(id);
        AlunoEntity aluno = new AlunoEntity();
        aluno.setId(id);
        aluno.setNome(alunoRequest.nome());
        aluno.setNascimento(alunoRequest.nascimento());

        AlunoEntity alunoAtualizado = alunoRepository.save(aluno);
        return alunoResponse(alunoAtualizado);
    }

    public void excluir(Long id) {
        buscarPorId(id);
        alunoRepository.deleteById(id);
    }

    private AlunoResponse alunoResponse(AlunoEntity aluno){
        return new AlunoResponse(aluno.getId(), aluno.getNome(), aluno.getNascimento());
    }
}
