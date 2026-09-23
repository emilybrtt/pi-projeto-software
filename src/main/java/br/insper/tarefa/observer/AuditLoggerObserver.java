package br.insper.tarefa.observer;

import br.insper.tarefa.entity.RegistroAuditoria;
import br.insper.tarefa.entity.Tarefa;
import br.insper.tarefa.entity.TipoOperacao;
import br.insper.tarefa.repository.RegistroAuditoriaRepository;
import br.insper.tarefa.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Component
public class AuditLoggerObserver implements TarefaObserver {

    private static final Logger logger = LoggerFactory.getLogger(AuditLoggerObserver.class);
    @Autowired
    private RegistroAuditoriaRepository auditoriaRepository;

    @Override
    public void criar(Tarefa tarefa) {
        registrar(tarefa, TipoOperacao.CREATE);
    }

    @Override
    public void deletar(Tarefa tarefa) {
        registrar(tarefa, TipoOperacao.DELETE);
    }

    private void registrar(Tarefa tarefa, TipoOperacao operacao) {
        logger.info("AUDITORIA - {} | Tarefa ID: {}", operacao, tarefa.getId());
        RegistroAuditoria a = new RegistroAuditoria();
        a.setData(LocalDateTime.now());
        a.setOperacao(operacao);
        a.setIdTarefa(tarefa.getId());
        auditoriaRepository.save(a);
    }
}
