package com.senai.miniprojeto02.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.miniprojeto02.controllers.dto.request.DisciplinaRequest;
import com.senai.miniprojeto02.controllers.dto.response.DisciplinaResponse;
import com.senai.miniprojeto02.entities.DisciplinaEntity;
import com.senai.miniprojeto02.entities.ProfessorEntity;
import com.senai.miniprojeto02.exception.NotFoundException;
import com.senai.miniprojeto02.repositories.DisciplinaRepository;
import com.senai.miniprojeto02.repositories.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DisciplinaService {
    private final DisciplinaRepository disciplinaRepository;
    private final ProfessorRepository professorRepository;
    private final ObjectMapper objectMapper;

    public List<DisciplinaResponse> buscarTodos() throws JsonProcessingException {
        log.info("Buscando todos os disciplinas do banco de dados.");
        List<DisciplinaEntity> disciplinas = disciplinaRepository.findAll();
        List<DisciplinaResponse> disciplinasResponse = new ArrayList<>();
        disciplinas.forEach(disciplina -> disciplinasResponse.add(
                disciplinaResponse(disciplina)
        ));
        log.info("Total de disciplinas encontrados: {}", disciplinas.size());
        String disciplinasJson = objectMapper.writeValueAsString(disciplinas);
        log.debug("Disciplinas encontrados: {}", disciplinasJson);
        return disciplinasResponse;
    }

    public DisciplinaResponse buscarPorId(Long id) throws JsonProcessingException {
        log.info("Buscando disciplina no banco de dados com id: {}", id);
        DisciplinaEntity disciplina = disciplinaRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Nenhum disciplina encontrado com o id: {}", id);
                    return new NotFoundException("Disciplina com id " + id + " não encontrado.");
                }
        );
        log.info("Disciplina encontrado.");
        String disciplinaJson = objectMapper.writeValueAsString(disciplina);
        log.debug("Dados do disciplina com id {}: {}", id,disciplinaJson);
        return disciplinaResponse(disciplina);
    }

    public DisciplinaResponse criar(DisciplinaRequest disciplinaRequest) throws Exception {
        log.info("Criando novo disciplina.");

        DisciplinaEntity disciplina = disciplinaEntity(disciplinaRequest);
        disciplina.setId(null);

        DisciplinaEntity disciplinaSalvo = disciplinaRepository.save(disciplina);
        log.info("Disciplina criado com sucesso.");

        String disciplinaJson = objectMapper.writeValueAsString(disciplinaSalvo);
        log.debug("Dados do disciplina: {}", disciplinaJson);

        return disciplinaResponse(disciplinaSalvo);
    }

    public DisciplinaResponse atualizar(Long id, DisciplinaRequest disciplinaRequest) throws Exception {
        log.info("Atualizando disciplina com id: {}", id);
        buscarPorId(id);

        DisciplinaEntity disciplina = disciplinaEntity(disciplinaRequest);
        disciplina.setId(id);

        DisciplinaEntity disciplinaAtualizado = disciplinaRepository.save(disciplina);
        log.info("Disciplina atualizado com sucesso.");

        String disciplinaJson = objectMapper.writeValueAsString(disciplinaAtualizado);
        log.debug("Dados do disciplina atualizado: {}", disciplinaJson);

        return disciplinaResponse(disciplinaAtualizado);
    }

    public void excluir(Long id) throws JsonProcessingException {
        log.info("Excluindo disciplina com id: {}", id);
        buscarPorId(id);
        disciplinaRepository.deleteById(id);
        log.info("Disciplina com id {} excluído sucesso.", id);
    }

    private DisciplinaResponse disciplinaResponse(DisciplinaEntity disciplina){
        return new DisciplinaResponse(disciplina.getId(), disciplina.getNome(), disciplina.getProfessor());
    }

    private DisciplinaEntity disciplinaEntity(DisciplinaRequest disciplinaRequest) throws BadRequestException {
        Long professorId= disciplinaRequest.professorId();
        ProfessorEntity professor = professorRepository.findById(professorId).orElseThrow(
                () -> {
                    log.error("Nenhum professor encontrado com o id: {}", professorId);
                    return new NotFoundException("Disciplina com id " + professorId + " não encontrado.");
                }
        );

        log.info("Adicionando dados a disciplina.");
        DisciplinaEntity disciplina = new DisciplinaEntity();
        validarNome(disciplinaRequest.nome());
        disciplina.setNome(disciplinaRequest.nome());
        disciplina.setProfessor(professor);
        return disciplina;
    }

    private void validarNome(String nome) throws BadRequestException {
        log.info("Validando nome.");
        if (
                nome == null ||
                nome.isBlank() ||
                nome.length() < 2
        ) {
            log.error("Nome do disciplina é inválido: {}", nome);
            throw new BadRequestException("Nome não pode estar em branco e deve ter ao menos 2 caracteres.");
        }
        log.info("Nome valido.");
    }
}
