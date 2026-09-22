package br.insper.simulado.observer;

import org.springframework.stereotype.Component;

import br.insper.simulado.entity.Curso;

@Component
public class EmailNotifierObserver implements CursoObserver {
    @Override
    public void atualizar(Curso curso, String statusAnterior, String statusNovo) {
        String mensagem = String.format(
                "EMAIL ENVIADO - Curso ID: %d mudou para status: %s",
                curso.getId(),
                statusNovo
        );
        System.out.println(mensagem);
    }
}
