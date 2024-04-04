package com.senai.miniprojeto02.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.miniprojeto02.controllers.dto.request.MatriculaRequest;
import com.senai.miniprojeto02.controllers.dto.response.MatriculaResponse;
import com.senai.miniprojeto02.entities.AlunoEntity;
import com.senai.miniprojeto02.entities.DisciplinaEntity;
import com.senai.miniprojeto02.entities.MatriculaEntity;
import com.senai.miniprojeto02.exception.NotFoundException;
import com.senai.miniprojeto02.repositories.AlunoRepository;
import com.senai.miniprojeto02.repositories.MatriculaRepository;
import com.senai.miniprojeto02.repositories.DisciplinaRepository;
import com.senai.miniprojeto02.repositories.NotaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatriculaService {
    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final NotaRepository notaRepository;
    private final ObjectMapper objectMapper;

    public List<MatriculaResponse> buscarTodos() throws JsonProcessingException {
        log.info("Buscando todas as matrículas.");
        List<MatriculaEntity> matriculas = matriculaRepository.findAll();
        log.info("Matrículas encontradas.");
        List<MatriculaResponse> matriculasResponses = new ArrayList<>();
        matriculas.forEach( matricula -> matriculasResponses.add(matriculaResponse(matricula)));
        log.info(
                "Total de matrículas encontradas {}.",
                matriculasResponses.size()
        );
        String matriculasJson = objectMapper.writeValueAsString(matriculas);
        log.debug("Matrículas encontrados: {}", matriculasJson);
        return matriculasResponses;
    }

    public MatriculaResponse buscarPorId(Long id) throws JsonProcessingException {
        log.info("Buscando matrícula no banco de dados com id: {}", id);
        MatriculaEntity matricula = matriculaRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Nenhum matrícula encontrada com o id: {}", id);
                    return new NotFoundException("Matrícula com id " + id + " não encontrada.");
                }
        );
        log.info("Matrícula encontrada.");
        String matriculaJson = objectMapper.writeValueAsString(matricula);
        log.debug("Dados do matrícula com id {}: {}", id, matriculaJson);
        return matriculaResponse(matricula);
    }

    public List<MatriculaResponse> buscarPorAlunoId(Long alunoId) throws JsonProcessingException {
        log.info("Buscando matrículas no banco de dados para aluno com id: {}", alunoId);
        List<MatriculaEntity> matriculas = matriculaRepository.findByAlunoId(alunoId);
        log.info("Matrículas encontradas.");
        List<MatriculaResponse> matriculasResponses = new ArrayList<>();
        matriculas.forEach( matricula -> matriculasResponses.add(matriculaResponse(matricula)));
        log.info(
                "Total de matrículas encontradas {} para o aluno id {}.",
                matriculasResponses.size(),
                alunoId
        );
        String matriculasJson = objectMapper.writeValueAsString(matriculas);
        log.debug("Matrículas encontrados: {}", matriculasJson);
        return matriculasResponses;
    }

    public List<MatriculaResponse> buscarPorDisciplinaId(Long disciplinaId) throws JsonProcessingException {
        log.info("Buscando matrículas no banco de dados para disciplina com id: {}", disciplinaId);
        List<MatriculaEntity> matriculas = matriculaRepository.findByDisciplinaId(disciplinaId);
        log.info("Matrículas encontradas.");
        List<MatriculaResponse> matriculasResponses = new ArrayList<>();
        matriculas.forEach( matricula -> matriculasResponses.add(matriculaResponse(matricula)));
        log.info(
                "Total de matrículas encontradas {} para a disciplina id {}.",
                matriculasResponses.size(),
                disciplinaId
        );
        String matriculasJson = objectMapper.writeValueAsString(matriculas);
        log.debug("Matrículas encontrados: {}", matriculasJson);
        return matriculasResponses;
    }

    public MatriculaResponse criar(MatriculaRequest matriculaRequest) throws Exception {
        log.info("Criando nova matrícula.");

        MatriculaEntity matricula = matriculaEntity(matriculaRequest);
        matricula.setId(null);
        LocalDate dataMatricula = LocalDate.now();
        matricula.setDataMatricula(dataMatricula);
        matricula.setMediaFinal(0.0);

        MatriculaEntity matriculaSalvo = matriculaRepository.save(matricula);
        log.info("Matrícula criada com sucesso.");

        String matriculaJson = objectMapper.writeValueAsString(matriculaSalvo);
        log.debug("Dados da matrícula: {}", matriculaJson);

        return matriculaResponse(matriculaSalvo);
    }

    public MatriculaResponse atualizar(Long id, MatriculaRequest matriculaRequest) throws Exception {
        log.info("Atualizando matrícula com id: {}", id);
        MatriculaResponse matriculaEntity = buscarPorId(id);

        MatriculaEntity matricula = matriculaEntity(matriculaRequest);
        matricula.setId(id);
        matricula.setDataMatricula(matriculaEntity.dataMatricula());
        matricula.setMediaFinal(matriculaEntity.mediaFinal());

        MatriculaEntity matriculaAtualizada = matriculaRepository.save(matricula);
        log.info("Matrícula atualizada com sucesso.");

        String matriculaJson = objectMapper.writeValueAsString(matriculaAtualizada);
        log.debug("Dados da matrícula atualizada: {}", matriculaJson);

        return matriculaResponse(matriculaAtualizada);
    }

    public void excluir(Long id) throws Exception {
        log.info("Excluindo matrícula com id: {}", id);
        buscarPorId(id);

        if(!notaRepository.findByDisciplinaId(id).isEmpty()) {
            log.error("Matrícula com id {} não pode ser excluída pois tem notas vinculadas a ela.", id);
            throw new BadRequestException(
                    "Matrícula com id " + id +
                    " não pode ser excluída pois tem notas vinculadas a ela."
            );
        }

        matriculaRepository.deleteById(id);
        log.info("Matrícula com id {} excluída com sucesso.", id);
    }

    private MatriculaResponse matriculaResponse(MatriculaEntity matricula){
        return new MatriculaResponse(
                matricula.getId(),
                matricula.getDataMatricula(),
                matricula.getMediaFinal(),
                matricula.getAluno(),
                matricula.getDisciplina()
        );
    }

    private MatriculaEntity matriculaEntity(MatriculaRequest matriculaRequest) throws BadRequestException {
        Long alunoId = matriculaRequest.alunoId();
        Long disciplinaId = matriculaRequest.disciplinaId();

        AlunoEntity aluno = alunoRepository.findById(alunoId).orElseThrow(
                () -> {
                    log.error("Nenhum aluno encontrada com o id: {}", alunoId);
                    return new NotFoundException("Aluno com id " + alunoId + " não encontrado.");
                }
        );
        DisciplinaEntity disciplina = disciplinaRepository.findById(disciplinaId).orElseThrow(
                () -> {
                    log.error("Nenhuma disciplina encontrada com o id: {}", disciplinaId);
                    return new NotFoundException("Disciplina com id " + disciplinaId + " não encontrada.");
                }
        );

        MatriculaEntity matricula = new MatriculaEntity();
        matricula.setAluno(aluno);
        matricula.setDisciplina(disciplina);

        return matricula;
    }
}
