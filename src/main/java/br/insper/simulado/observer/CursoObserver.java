package br.insper.simulado.observer;

import br.insper.simulado.entity.Curso;

public interface CursoObserver {
    void atualizar(Curso curso, String statusAnterior, String statusNovo);
}
