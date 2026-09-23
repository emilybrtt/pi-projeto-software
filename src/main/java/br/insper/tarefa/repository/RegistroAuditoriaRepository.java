package br.insper.tarefa.repository;

import br.insper.tarefa.entity.RegistroAuditoria;
import br.insper.tarefa.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RegistroAuditoriaRepository extends JpaRepository<RegistroAuditoria, Long>{
}
