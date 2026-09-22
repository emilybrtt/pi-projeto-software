package br.insper.simulado.repository;
import br.insper.simulado.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByDeletadoFalse();
    List<Curso> findByDeletadoFalseAndNomeStartingWith(String nome);
}
