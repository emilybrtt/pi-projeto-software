Exercício - 14/09/2026

Implemente a seguinte API em Java com o Spring Boot:

Aplicação de tarefas online

A aplicação deve conter as seguintes rotas:

GET /tarefas

Lista todos os tarefas. Não devem ser mostrados tarefas deletados.

Incluir um filtro pelo nome do tarefa. O filtro deve ser do tipo startsWith, isto é, devem ser retornados todos os tarefas cujo nome se inicia com a string enviada no filtro.

POST /tarefas

Cria um tarefa no banco de dados. (Podem pensar nos atributos que a classe tarefa deve ter.)

DELETE /tarefas/{id}

Deleta um tarefa selecionado. A deleção do tarefa deve ser apenas lógica.

Requisitos

A aplicação deve se conectar a um banco de dados PostgreSQL configurado na máquina da AWS.

A aplicação deve ser configurada para o deploy automático usando o GitHub Actions. Também deve ser incluído o pipeline para a execução de testes.

Todos os dados sensíveis, como senhas, IPs etc., devem ser configurados usando secrets e variáveis de ambiente.

A aplicação deve ter cobertura de testes de 100% na classe de serviço e ter um teste de integração para as três rotas pedidas.

Uma das rotas deve ser criada via um Pull Request no Git, para executar o pipeline de testes.