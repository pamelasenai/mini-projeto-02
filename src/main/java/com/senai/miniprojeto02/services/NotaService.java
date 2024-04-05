package com.senai.miniprojeto02.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.miniprojeto02.controllers.dto.request.NotaRequest;
import com.senai.miniprojeto02.controllers.dto.response.NotaResponse;
import com.senai.miniprojeto02.entities.DisciplinaEntity;
import com.senai.miniprojeto02.entities.MatriculaEntity;
import com.senai.miniprojeto02.entities.NotaEntity;
import com.senai.miniprojeto02.entities.ProfessorEntity;
import com.senai.miniprojeto02.exception.NotFoundException;
import com.senai.miniprojeto02.repositories.*;
import com.senai.miniprojeto02.repositories.NotaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotaService {
    private final NotaRepository notaRepository;
    private final MatriculaRepository matriculaRepository;
    private final ObjectMapper objectMapper;

    public List<NotaResponse> buscarTodos() throws JsonProcessingException {
        log.info("Buscando todas as notas.");
        List<NotaEntity> notas = notaRepository.findAll();
        log.info("Notas encontradas.");
        List<NotaResponse> notasResponses = new ArrayList<>();
        notas.forEach( nota -> notasResponses.add(notaResponse(nota)));
        log.info(
                "Total de notas encontradas {}.",
                notasResponses.size()
        );
        String notasJson = objectMapper.writeValueAsString(notas);
        log.debug("Notas encontrados: {}", notasJson);
        return notasResponses;
    }

    public NotaResponse buscarPorId(Long id) throws JsonProcessingException {
        log.info("Buscando nota no banco de dados com id: {}", id);
        NotaEntity nota = notaRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Nenhuma nota encontrada com o id: {}", id);
                    return new NotFoundException("Nota com id " + id + " não encontrada.");
                }
        );
        log.info("Nota encontrada.");
        String notaJson = objectMapper.writeValueAsString(nota);
        log.debug("Dados da nota com id {}: {}", id, notaJson);
        return notaResponse(nota);
    }

    public List<NotaResponse> buscarPorMatriculaId(Long matriculaId) throws JsonProcessingException {
        log.info("Buscando notas no banco de dados para matrícula com id: {}", matriculaId);
        List<NotaEntity> notas = notaRepository.findByMatriculaId(matriculaId);
        log.info("Notas encontradas.");
        List<NotaResponse> notasResponses = new ArrayList<>();
        notas.forEach( nota -> notasResponses.add(notaResponse(nota)));
        log.info(
                "Total de notas encontradas {} para a matrícula id {}.",
                notasResponses.size(),
                matriculaId
        );
        String notasJson = objectMapper.writeValueAsString(notas);
        log.debug("Notas encontrados: {}", notasJson);
        return notasResponses;
    }

    public NotaResponse criar(NotaRequest notaRequest) throws Exception {
        log.info("Criando nova nota.");

        double coeficienteAnterior = 0.0;
        NotaEntity nota = notaEntity(notaRequest, coeficienteAnterior);
        nota.setId(null);

        NotaEntity notaSalvo = notaRepository.saveAndFlush(nota);
        log.info("Nota criada com sucesso.");

        String notaJson = objectMapper.writeValueAsString(notaSalvo);
        log.debug("Dados da nota: {}", notaJson);

        somarNotaMediaFinal(notaRequest.matriculaId());
        return notaResponse(notaSalvo);
    }

    public NotaResponse atualizar(Long id, NotaRequest notaRequest) throws Exception {
        log.info("Atualizando nota com id: {}", id);
        NotaResponse nota = buscarPorId(id);
        double coeficienteAnterior = nota.coeficiente();

        NotaEntity novaNota = notaEntity(notaRequest, coeficienteAnterior);
        novaNota.setId(id);

        NotaEntity notaAtualizada = notaRepository.saveAndFlush(novaNota);
        log.info("Nota atualizada com sucesso.");

        String notaJson = objectMapper.writeValueAsString(notaAtualizada);
        log.debug("Dados da nota atualizada: {}", notaJson);

        somarNotaMediaFinal(notaRequest.matriculaId());
        return notaResponse(notaAtualizada);
    }

    public void excluir(Long id) throws Exception {
        log.info("Excluindo nota com id: {}", id);
        NotaResponse notaEntity = buscarPorId(id);
        MatriculaEntity matricula = notaEntity.matricula();
        Long matriculaId = matricula.getId();
        subtrairNotaMediaFinal(matriculaId, notaEntity);

        notaRepository.deleteById(id);
        log.info("Nota com id {} excluída com sucesso.", id);
    }

    private NotaResponse notaResponse(NotaEntity nota){
        return new NotaResponse(
                nota.getId(),
                nota.getNota(),
                nota.getCoeficiente(),
                nota.getMatricula(),
                nota.getProfessor()
        );
    }

    private NotaEntity notaEntity(NotaRequest notaRequest, double coeficienteAnterior) throws BadRequestException {
        Long matriculaId = notaRequest.matriculaId();
        MatriculaEntity matricula = buscarMatriculaPorId(matriculaId);
        DisciplinaEntity disciplina = matricula.getDisciplina();
        ProfessorEntity professor = disciplina.getProfessor();

        validarCoeficiente(notaRequest, coeficienteAnterior);

        log.info("Adicionando dados a nota.");

        NotaEntity nota = new NotaEntity();
        nota.setNota(notaRequest.nota());
        nota.setCoeficiente(notaRequest.coeficiente());
        nota.setMatricula(matricula);
        nota.setProfessor(professor);

        return nota;
    }

    private Double somaCoeficientePorMatriculaId(Long matriculaId, double coeficienteAnterior) {
        log.info("Somando coeficiêntes anteriores.");
        List<NotaEntity> notas = notaRepository.findByMatriculaId(matriculaId);

        double somaCoeficiente = 0.0;
        if(!notas.isEmpty()) {
            for (NotaEntity nota : notas) {
                somaCoeficiente += nota.getCoeficiente();
            }
        }
        somaCoeficiente -= coeficienteAnterior;

        log.info("Coeficiêntes somados, total: {}.", somaCoeficiente);
        return somaCoeficiente;
    }

    private void validarCoeficiente(NotaRequest notaRequest, double coeficienteAnterior) throws BadRequestException {
        log.info("Validando coeficiênte.");
        Long matriculaId = notaRequest.matriculaId();
        Double coeficiente = notaRequest.coeficiente();
        Double somaCoeficiente = somaCoeficientePorMatriculaId(matriculaId, coeficienteAnterior);

        if (
                notaRequest.coeficiente() < 0 ||
                notaRequest.coeficiente() > 1 ||
                somaCoeficiente + coeficiente > 1 ||
                somaCoeficiente + coeficiente < 0
        ){
            log.error(
                    "Coeficiênte invalido: {}. " +
                    "A soma dos coeficiêntes desta matrícula não pode exceder a 1. " +
                    "Soma dos coeficiêntes desta matrícula até o momento: {}",
                    coeficiente,
                    somaCoeficiente
            );
            throw new BadRequestException(
                    "Coeficiênte invalido: " + coeficiente + ". " +
                    "A soma dos coeficiêntes desta matrícula não pode exceder a 1. " +
                    "Soma dos coeficiêntes desta matrícula até o momento: " + somaCoeficiente
            );
        }
        log.info("Coeficiênte valido.");
    }

    private MatriculaEntity buscarMatriculaPorId(Long matriculaId){
        log.info("Buscando matrícula por Id.");
        return matriculaRepository.findById(matriculaId).orElseThrow(
                () -> {
                    log.error("Nenhuma matrícula encontrada com o id: {}", matriculaId);
                    return new NotFoundException("Matrícula com id " + matriculaId + " não encontrada.");
                }
        );
    }

    private void somarNotaMediaFinal(Long matriculaId) {
        log.info("Atualizando média final.");
        MatriculaEntity matricula = buscarMatriculaPorId(matriculaId);
        List<NotaEntity> notas = notaRepository.findByMatriculaId(matriculaId);
        double mediaFinal = 0.0;

        log.info("Calculando média final.");
        for (NotaEntity nota : notas) {
            mediaFinal = mediaFinal + (nota.getNota() * nota.getCoeficiente());
        }
        matricula.setMediaFinal(mediaFinal);
        log.info("Calculo da média final concluído, média final: {}.", mediaFinal);

        matriculaRepository.saveAndFlush(matricula);
        log.info("Média final atualizada.");
    }

    private void subtrairNotaMediaFinal(Long matriculaId, NotaResponse notaEntity) {
        log.info("Atualizando média final.");
        MatriculaEntity matricula = buscarMatriculaPorId(matriculaId);
        double mediaFinal = matricula.getMediaFinal();

        log.info("Calculando média final.");
        double nota = notaEntity.nota() * notaEntity.coeficiente();
        mediaFinal = mediaFinal - nota;

        matricula.setMediaFinal(mediaFinal);
        log.info("Calculo da média final concluído, média final: {}.", mediaFinal);

        matriculaRepository.saveAndFlush(matricula);
        log.info("Média final atualizada.");
    }
}
