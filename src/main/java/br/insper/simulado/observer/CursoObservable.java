package br.insper.simulado.observer;

import br.insper.simulado.entity.Curso;

public interface CursoObservable {
    void notificarObservadores(Curso curso, String statusAnterior);

}
