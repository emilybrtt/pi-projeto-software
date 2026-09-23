# Pergunta 1
## Sistema de Tarefas
Implementar uma API REST para gerenciar tarefas com **padrão Observable** para auditoria e notificações.
### Entidade
**Tarefa:** id, titulo, descricao, status (TODO/DOING/DONE), prioridade (ALTA/MEDIA/BAIXA), dataCriacao.
### Requisitos
- Rotas CRUD (criar, listar, buscar por id e excluir). 
- Implementar dois requisitos com o Padrão Observable

- **Padrão Observable 1: Auditoria**
    - Registrar em uma tabela do banco de dados cada ação (CREATE e DELETE) com: timestamp e tipo da operação.
    - Implementar observer que persiste eventos de auditoria.

- **Padrão Observable 2: Notificação**
    - Quando uma tarefa é criada com prioridade ALTA, notificar via console/log com a mensagem de alerta. 
    - Envie o link do GitHub com o código da API. A implementaçã da API vale 2 pontos, e a implementação dos requisitos com o padrão observable também 2 pontos.

# Pergunta 2
Implemente testes de unidade com 100% de cobertura na classe de serviço (2 pts). Envie um print do arquivo do JaCoCO.

# Pergunta 3
Implemente um teste de integração para a rota de criação (POST) (2 pts)
Coloque o link do GitHub da classe do teste implementado.

# Pergunta 4
Implemente o CI de deploy do projeto sua máquina da AWS.
Anexe um print de uma chamada do Postman para a aplicação, mostrando que a chamada funcionou e com o IP da máquina da AWS.

# Regras
O uso de LLMs não é permitido, especialmente em ferramentas como Claude Code e Codex
Comunicação é proibida
Pode ouvir música com fone, em um volume baixo
Utilize o Postgres como base de dados