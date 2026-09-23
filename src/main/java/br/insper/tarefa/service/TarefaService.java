package br.insper.tarefa.service;

import br.insper.tarefa.entity.Tarefa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.insper.tarefa.dto.TarefaDto;
import br.insper.tarefa.observer.TarefaObserver;
import br.insper.tarefa.observer.TarefaObservable;
import br.insper.tarefa.repository.TarefaRepository;
import br.insper.tarefa.validator.ValidadorTarefa;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TarefaService implements TarefaObservable {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired(required = false)
    private List<TarefaObserver> observers;

    @Override
    public void notificarObservadores(Tarefa tarefa, String prioridade) {
        if (observers != null) {
            for (TarefaObserver observer : observers) {
                observer.criar(tarefa);
            }
        }
    }
 // OBSERVER PRA TAREFA DE PRIORIDADE ALTA
    public Tarefa criar(TarefaDto dto) {
        Tarefa tarefa = Tarefa.fromDto(dto);
        validarTarefa(dto);
        Tarefa salvo = tarefaRepository.save(tarefa);
        notificarObservadores(salvo, String.valueOf(salvo.getPrioridade()));
        return salvo;
    }

    public List<Tarefa> listarTodos() {
        return tarefaRepository.findByDeletadoFalse();
    }


    public Optional<Tarefa> obterPorId(Long id) {
        return tarefaRepository.findById(id);
    }

    public boolean deletar(Long id) {
        Optional<Tarefa> tarefaOpt = tarefaRepository.findById(id);
        if (tarefaOpt.isPresent()) {
            Tarefa tarefa = tarefaOpt.get();
            tarefa.setDeletado(true);
            tarefa.setDataDelecao(java.time.LocalDateTime.now());
            tarefaRepository.save(tarefa);
            if (observers != null) {
                for (TarefaObserver observer : observers) {
                    observer.deletar(tarefa);
                }
            }
            return true;
        }
        return false;
    }

    private void validarTarefa(TarefaDto dto) {
        ValidadorTarefa validadorTarefaBase = new ValidadorTarefa();
        validadorTarefaBase.validarCamposObrigatorios(dto);
    }
}
