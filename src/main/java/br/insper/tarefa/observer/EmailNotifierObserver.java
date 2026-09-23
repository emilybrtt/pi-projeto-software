package br.insper.tarefa.observer;

import br.insper.tarefa.entity.PrioridadeTarefa;
import br.insper.tarefa.entity.Tarefa;
import org.springframework.stereotype.Component;

@Component
public class EmailNotifierObserver implements TarefaObserver {
    @Override
    public void criar(Tarefa tarefa) {
        String mensagem = String.format(
                "Tarefa ID: %d criada com prioridade ALTA!",
                tarefa.getId()
        );
        if(tarefa.getPrioridade() == PrioridadeTarefa.ALTA) {
            System.out.println(mensagem);
        }
    }
}
