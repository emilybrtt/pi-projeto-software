package br.insper.tarefa.observer;

import br.insper.tarefa.entity.Tarefa;

public interface TarefaObservable {
    void notificarObservadores(Tarefa tarefa, String prioridade);

}
