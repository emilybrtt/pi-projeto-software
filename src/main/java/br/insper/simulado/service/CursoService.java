package br.insper.simulado.service;

import br.insper.simulado.processor.Processador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.insper.simulado.dto.CursoDto;
import br.insper.simulado.entity.Curso;
import br.insper.simulado.exception.ValidacaoCursoException;
import br.insper.simulado.observer.CursoObserver;
import br.insper.simulado.observer.CursoObservable;
import br.insper.simulado.repository.CursoRepository;
import br.insper.simulado.validator.ValidadorCurso;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CursoService implements CursoObservable {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private Map<String, Processador> processadores;

    @Autowired(required = false)
    private List<CursoObserver> observers;

    @Override
    public void notificarObservadores(Curso curso, String statusAnterior) {
        if (observers != null) {
            for (CursoObserver observer : observers) {
                observer.atualizar(curso, statusAnterior, curso.getStatus());
            }
        }
    }

    public Curso criar(CursoDto dto) {
        validarCurso(dto);
        Curso curso = Curso.fromDto(dto);
        Curso salvo = cursoRepository.save(curso);
        notificarObservadores(salvo, null);
        return salvo;
    }

    public List<Curso> listarTodos() {
        return cursoRepository.findByDeletadoFalse();
    }

    public List<Curso> listarPorNome(String nome) {
        return cursoRepository.findByDeletadoFalseAndNomeStartingWith(nome);
    }

    public Optional<Curso> obterPorId(Long id) {
        return cursoRepository.findById(id);
    }

    public boolean deletar(Long id) {
        Optional<Curso> cursoOpt = cursoRepository.findById(id);
        if (cursoOpt.isPresent()) {
            Curso curso = cursoOpt.get();
            curso.setDeletado(true);
            cursoRepository.save(curso);
            return true;
        }
        return false;
    }

    public boolean processar(Long id) {
        Curso curso = cursoRepository
                .findById(id)
                .orElseThrow(() -> new ValidacaoCursoException("Curso com ID " + id + " não encontrado"));

        String statusAnterior = curso.getStatus();

        Processador processador = processadores.get(curso.getTipo().toString());
        boolean sucesso = processador.processar(curso);

        cursoRepository.save(curso);

        if (sucesso) {
            notificarObservadores(curso, statusAnterior);
        }

        return sucesso;
    }

    private void validarCurso(CursoDto dto) {
        ValidadorCurso validadorCursoBase = new ValidadorCurso();
        validadorCursoBase.validarCamposObrigatorios(dto);
    }
}
