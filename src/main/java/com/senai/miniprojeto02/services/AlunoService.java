package com.senai.miniprojeto02.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.miniprojeto02.controllers.dto.request.AlunoRequest;
import com.senai.miniprojeto02.controllers.dto.response.AlunoResponse;
import com.senai.miniprojeto02.entities.AlunoEntity;
import com.senai.miniprojeto02.exception.NotFoundException;
import com.senai.miniprojeto02.repositories.AlunoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlunoService {
    private final AlunoRepository alunoRepository;
    private final ObjectMapper objectMapper;

    public List<AlunoResponse> buscarTodos() throws JsonProcessingException {
        log.info("Buscando todos os alunos do banco de dados.");
        List<AlunoEntity> alunos = alunoRepository.findAll();
        List<AlunoResponse> alunosResponse = new ArrayList<>();
        alunos.forEach(aluno -> alunosResponse.add(
                alunoResponse(aluno)
        ));
        log.info("Total de alunos encontrados: {}", alunos.size());
        String alunosJson = objectMapper.writeValueAsString(alunos);
        log.debug("Alunos encontrados: {}", alunosJson);
        return alunosResponse;
    }

    public AlunoResponse buscarPorId(Long id) throws JsonProcessingException {
        log.info("Buscando aluno no banco de dados com id: {}", id);
        AlunoEntity aluno = alunoRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Nenhum aluno encontrado com o id: {}", id);
                    return new NotFoundException("Aluno com id " + id + " não encontrado.");
                }
        );
        log.info("Aluno encontrado.");
        String alunoJson = objectMapper.writeValueAsString(aluno);
        log.debug("Dados do aluno com id {}: {}", id,alunoJson);
        return alunoResponse(aluno);
    }

    public AlunoResponse criar(AlunoRequest alunoRequest) throws Exception {
        log.info("Criando novo aluno.");

        AlunoEntity aluno = alunoEntity(alunoRequest);
        aluno.setId(null);

        AlunoEntity alunoSalvo = alunoRepository.save(aluno);
        log.info("Aluno criado com sucesso.");

        String alunoJson = objectMapper.writeValueAsString(alunoSalvo);
        log.debug("Dados do aluno: {}", alunoJson);

        return alunoResponse(alunoSalvo);
    }

    public AlunoResponse atualizar(Long id, AlunoRequest alunoRequest) throws Exception {
        log.info("Atualizando aluno com id: {}", id);
        buscarPorId(id);

        AlunoEntity aluno = alunoEntity(alunoRequest);
        aluno.setId(id);

        AlunoEntity alunoAtualizado = alunoRepository.save(aluno);
        log.info("Aluno atualizado com sucesso.");

        String alunoJson = objectMapper.writeValueAsString(alunoAtualizado);
        log.debug("Dados do aluno atualizado: {}", alunoJson);

        return alunoResponse(alunoAtualizado);
    }

    public void excluir(Long id) throws JsonProcessingException {
        log.info("Excluindo aluno com id: {}", id);
        buscarPorId(id);
        alunoRepository.deleteById(id);
        log.info("Aluno com id {} excluído sucesso.", id);
    }

    private AlunoResponse alunoResponse(AlunoEntity aluno){
        return new AlunoResponse(aluno.getId(), aluno.getNome(), aluno.getNascimento());
    }

    private AlunoEntity alunoEntity(AlunoRequest alunoRequest) throws BadRequestException {
        AlunoEntity aluno = new AlunoEntity();
        validarNome(alunoRequest.nome());
        aluno.setNome(alunoRequest.nome());
        aluno.setNascimento(formatarData(alunoRequest.nascimento()));
        return aluno;
    }

    private LocalDate formatarData(String dataString) {
        LocalDate nascimento;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(dataString, formatter);
        } catch (DateTimeParseException e) {
            log.error("Data de nascimento inválida: {} . Por não ser um dado obrigatório ficará como null.", dataString);
            return null;
        }
    }

    private void validarNome(String nome) throws BadRequestException {
        if (
                nome == null ||
                nome.isBlank() ||
                nome.length() < 3
        ) {
            log.error("Nome do aluno é inválido: {}", nome);
            throw new BadRequestException("Nome não pode estar em branco e deve ter ao menos 3 caracteres.");
        }
    }
}
