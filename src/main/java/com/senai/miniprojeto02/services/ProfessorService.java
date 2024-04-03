package com.senai.miniprojeto02.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.miniprojeto02.controllers.dto.request.ProfessorRequest;
import com.senai.miniprojeto02.controllers.dto.response.ProfessorResponse;
import com.senai.miniprojeto02.entities.ProfessorEntity;
import com.senai.miniprojeto02.exception.NotFoundException;
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
public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private final ObjectMapper objectMapper;

    public List<ProfessorResponse> buscarTodos() throws JsonProcessingException {
        log.info("Buscando todos os professores do banco de dados.");
        List<ProfessorEntity> professores = professorRepository.findAll();
        List<ProfessorResponse> professoresResponse = new ArrayList<>();
        professores.forEach(professor -> professoresResponse.add(
                professorResponse(professor)
        ));
        log.info("Total de professores encontrados: {}", professores.size());
        String professoresJson = objectMapper.writeValueAsString(professores);
        log.debug("Professors encontrados: {}", professoresJson);
        return professoresResponse;
    }

    public ProfessorResponse buscarPorId(Long id) throws JsonProcessingException {
        log.info("Buscando professor no banco de dados com id: {}", id);
        ProfessorEntity professor = professorRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Nenhum professor encontrado com o id: {}", id);
                    return new NotFoundException("Professor com id " + id + " não encontrado.");
                }
        );
        log.info("Professor encontrado.");
        String professorJson = objectMapper.writeValueAsString(professor);
        log.debug("Dados do professor com id {}: {}", id,professorJson);
        return professorResponse(professor);
    }

    public ProfessorResponse criar(ProfessorRequest professorRequest) throws Exception {
        log.info("Criando novo professor.");

        ProfessorEntity professor = professorEntity(professorRequest);
        professor.setId(null);

        ProfessorEntity professorSalvo = professorRepository.save(professor);
        log.info("Professor criado com sucesso.");

        String professorJson = objectMapper.writeValueAsString(professorSalvo);
        log.debug("Dados do professor: {}", professorJson);

        return professorResponse(professorSalvo);
    }

    public ProfessorResponse atualizar(Long id, ProfessorRequest professorRequest) throws Exception {
        log.info("Atualizando professor com id: {}", id);
        buscarPorId(id);

        ProfessorEntity professor = professorEntity(professorRequest);
        professor.setId(id);

        ProfessorEntity professorAtualizado = professorRepository.save(professor);
        log.info("Professor atualizado com sucesso.");

        String professorJson = objectMapper.writeValueAsString(professorAtualizado);
        log.debug("Dados do professor atualizado: {}", professorJson);

        return professorResponse(professorAtualizado);
    }

    public void excluir(Long id) throws JsonProcessingException {
        log.info("Excluindo professor com id: {}", id);
        buscarPorId(id);
        professorRepository.deleteById(id);
        log.info("Professor com id {} excluído sucesso.", id);
    }

    private ProfessorResponse professorResponse(ProfessorEntity professor){
        return new ProfessorResponse(professor.getId(), professor.getNome());
    }

    private ProfessorEntity professorEntity(ProfessorRequest professorRequest) throws BadRequestException {
        ProfessorEntity professor = new ProfessorEntity();
        validarNome(professorRequest.nome());
        professor.setNome(professorRequest.nome());
        return professor;
    }

    private void validarNome(String nome) throws BadRequestException {
        if (
                nome == null ||
                nome.isBlank() ||
                nome.length() < 3
        ) {
            log.error("Nome do professor é inválido: {}", nome);
            throw new BadRequestException("Nome não pode estar em branco e deve ter ao menos 3 caracteres.");
        }
    }
}
