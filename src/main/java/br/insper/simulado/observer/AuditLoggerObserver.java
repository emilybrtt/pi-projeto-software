package br.insper.simulado.observer;

import br.insper.simulado.entity.Curso;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AuditLoggerObserver implements CursoObserver {

    private static final Logger logger = LoggerFactory.getLogger(AuditLoggerObserver.class);

    @Override
    public void atualizar(Curso curso, String statusAnterior, String statusNovo) {
        String mensagem = String.format(
            "AUDITORIA - Curso ID: %d | Status: %s → %s | Tipo: %s",
            curso.getId(),
            statusAnterior,
            statusNovo,
            curso.getTipo()
        );
        logger.info(mensagem);
    }
}
