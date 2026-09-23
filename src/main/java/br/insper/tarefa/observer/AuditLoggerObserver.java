package br.insper.tarefa.observer;

import br.insper.tarefa.entity.Tarefa;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AuditLoggerObserver implements TarefaObserver {

    private static final Logger logger = LoggerFactory.getLogger(AuditLoggerObserver.class);

    @Override
    public void criar(Tarefa tarefa) {
        String mensagem = String.format(
                "AUDITORIA - Tarefa ID: %d | Prioridade: %s | Criada em: %s | Deletada em: %s",
                tarefa.getId(),
                tarefa.getPrioridade().toString(),
                tarefa.getDataCriacao(),
                tarefa.getDataDelecao() != null
                        ? tarefa.getDataDelecao()
                        : "N/A"
        );

        logger.info(mensagem);
    }
}
