package br.insper.simulado.controller;

import br.insper.simulado.dto.CursoDto;
import br.insper.simulado.entity.Curso;
import br.insper.simulado.entity.TipoCurso;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class CursoControllerTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("simulado_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test_shouldCreateCurso() throws Exception {

        CursoDto dto = new CursoDto();
        dto.setNome("Engenharia de Software");
        dto.setTipo(TipoCurso.GRADUACAO);
        dto.setValor(1000.0);
        dto.setDescricao("Curso de Graduação em Engenharia de Software");
        dto.setProfessor("Fulano de Tal");

        // chamada
        MvcResult result = mockMvc.perform(
                        post("/api/cursos")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        // asserts
        Curso curso = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Curso.class);
        Assertions.assertNotNull(curso.getId());
        Assertions.assertEquals(TipoCurso.GRADUACAO, curso.getTipo());
        Assertions.assertEquals("Engenharia de Software", curso.getNome());
        Assertions.assertFalse(curso.getDeletado());
    }

    @Test
    public void test_shouldListarTodosOsCursos() throws Exception {
        // Criar um curso primeiro para garantir que a lista não vem vazia
        CursoDto dto = new CursoDto();
        dto.setNome("Medicina");
        dto.setTipo(TipoCurso.GRADUACAO);
        dto.setValor(5000.0);
        dto.setDescricao("Curso de graduação em Medicina");
        dto.setProfessor("Dra. Silvia");

        mockMvc.perform(post("/api/cursos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        // Testar a listagem
        MvcResult result = mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andReturn();

        Curso[] cursos = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Curso[].class);

        Assertions.assertTrue(cursos.length > 0);
    }

    @Test
    public void test_shouldNotListarCursosDeletados() throws Exception {
        // Criar um curso
        CursoDto dto = new CursoDto();
        dto.setNome("Física");
        dto.setTipo(TipoCurso.GRADUACAO);
        dto.setValor(3000.0);
        dto.setDescricao("Curso de Física");
        dto.setProfessor("Dr. Einstein");

        MvcResult createResult = mockMvc.perform(post("/api/cursos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        Curso cursoCriado = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                Curso.class);

        // Deletar o curso
        mockMvc.perform(delete("/api/cursos/{id}", cursoCriado.getId()))
                .andExpect(status().isNoContent());

        // Listar cursos e verificar que o deletado não aparece
        MvcResult listResult = mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andReturn();

        Curso[] cursos = objectMapper.readValue(
                listResult.getResponse().getContentAsString(),
                Curso[].class);

        for (Curso c : cursos) {
            Assertions.assertNotEquals(cursoCriado.getId(), c.getId());
        }
    }

    @Test
    public void test_shouldFilterCursosByNome() throws Exception {
        // Criar cursos com nomes diferentes
        CursoDto dto1 = new CursoDto();
        dto1.setNome("Engenharia Civil");
        dto1.setTipo(TipoCurso.GRADUACAO);
        dto1.setValor(2500.0);
        dto1.setDescricao("Curso de Engenharia Civil");
        dto1.setProfessor("Prof. João");

        CursoDto dto2 = new CursoDto();
        dto2.setNome("Engenharia Mecânica");
        dto2.setTipo(TipoCurso.GRADUACAO);
        dto2.setValor(2600.0);
        dto2.setDescricao("Curso de Engenharia Mecânica");
        dto2.setProfessor("Prof. Maria");

        CursoDto dto3 = new CursoDto();
        dto3.setNome("Medicina Veterinária");
        dto3.setTipo(TipoCurso.GRADUACAO);
        dto3.setValor(4000.0);
        dto3.setDescricao("Curso de Medicina Veterinária");
        dto3.setProfessor("Dra. Ana");

        mockMvc.perform(post("/api/cursos")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/cursos")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/cursos")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto3)))
                .andExpect(status().isCreated());

        // Filtrar por "Engenharia"
        MvcResult result = mockMvc.perform(get("/api/cursos?nome=Engenharia"))
                .andExpect(status().isOk())
                .andReturn();

        Curso[] cursos = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Curso[].class);

        // Verifica que retornou cursos que começam com "Engenharia"
        Assertions.assertTrue(cursos.length >= 2);
        for (Curso c : cursos) {
            Assertions.assertTrue(c.getNome().startsWith("Engenharia"));
        }
    }

    @Test
    public void test_shouldReturnCursoQuandoObterCursoExistente() throws Exception {
        // Preparação: Criar um curso
        CursoDto dto = new CursoDto();
        dto.setNome("Arquitetura");
        dto.setTipo(TipoCurso.GRADUACAO);
        dto.setValor(1200.0);
        dto.setDescricao("Curso de Arquitetura");
        dto.setProfessor("Maria");

        MvcResult postResult = mockMvc.perform(post("/api/cursos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        Curso cursoCriado = objectMapper.readValue(
                postResult.getResponse().getContentAsString(),
                Curso.class);

        // Ação: Obter o curso pelo ID gerado
        MvcResult getResult = mockMvc.perform(get("/api/cursos/{id}", cursoCriado.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Curso cursoObtido = objectMapper.readValue(
                getResult.getResponse().getContentAsString(),
                Curso.class);

        // Validação
        Assertions.assertEquals(cursoCriado.getId(), cursoObtido.getId());
        Assertions.assertEquals("Arquitetura", cursoObtido.getNome());
    }

    @Test
    public void test_shouldReturnNotFoundWhenObterCursoInexistente() throws Exception {
        mockMvc.perform(get("/api/cursos/{id}", 99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_shouldReturnNotFoundWhenDeletarCursoInexistente() throws Exception {
        mockMvc.perform(delete("/api/cursos/{id}", 99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_shouldReturnNoContentWhenDeletarCursoExistente() throws Exception {
        CursoDto dto = new CursoDto();
        dto.setNome("Ciência da Computação");
        dto.setTipo(TipoCurso.MBA);
        dto.setValor(2000.0);
        dto.setDescricao("MBA em Ciência da Computação");
        dto.setProfessor("Ciclano de Tal");

        MvcResult result = mockMvc.perform(
                        post("/api/cursos")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        Curso curso = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Curso.class);

        mockMvc.perform(delete("/api/cursos/{id}", curso.getId()))
                .andExpect(status().isNoContent());

        // Verifica que o curso foi logicamente deletado, não fisicamente removido
        MvcResult getResult = mockMvc.perform(get("/api/cursos/{id}", curso.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Curso cursoDepois = objectMapper.readValue(
                getResult.getResponse().getContentAsString(),
                Curso.class);

        Assertions.assertTrue(cursoDepois.getDeletado());
    }

    @Test
    public void test_shouldProcessarCursoExistente() throws Exception {
        // Preparação
        CursoDto dto = new CursoDto();
        dto.setNome("Direito");
        dto.setTipo(TipoCurso.GRADUACAO);
        dto.setValor(2000.0);
        dto.setDescricao("Curso de Direito");
        dto.setProfessor("Pedro");

        MvcResult postResult = mockMvc.perform(post("/api/cursos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        Curso cursoCriado = objectMapper.readValue(
                postResult.getResponse().getContentAsString(),
                Curso.class);

        // Ação e Validação do endpoint de processamento
        mockMvc.perform(post("/api/cursos/{id}/processar", cursoCriado.getId()))
                .andExpect(status().isOk());
    }
}