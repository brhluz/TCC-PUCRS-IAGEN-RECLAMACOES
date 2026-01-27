package br.com.pucrs.tcc.resource;

import br.com.pucrs.tcc.domain.ai.ClassificacaoItem;
import br.com.pucrs.tcc.domain.ai.ClassificacaoResponse;
import br.com.pucrs.tcc.domain.ai.ReclamacaoAiService;
import br.com.pucrs.tcc.domain.ai.Taxonomia;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
public class ReclamacaoResourceTest {

    @InjectMock
    ReclamacaoAiService aiService;

    @BeforeEach
    void setupAiMock() {
        ClassificacaoItem item = new ClassificacaoItem();
        item.setDepartamento(Taxonomia.Departamento.ATENDIMENTO_CLIENTE);
        item.setCategoria(Taxonomia.Categoria.ATRASO_ENTREGA);
        item.setMotivoExtraido("Atraso na entrega");

        ClassificacaoResponse response = new ClassificacaoResponse();
        response.setClassificacoes(List.of(item));

        when(aiService.classificar(anyString())).thenReturn(response);
    }

    @Test
    public void deveCriarReclamacaoComSucesso() {
        String requestBody = """
            {
                "nome": "João Silva",
                "email": "joao@teste.com",
                "descricao": "Produto chegou com defeito"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/reclamacoes")
        .then()
            .statusCode(201)
            .body("protocolo", notNullValue())
            .body("status", equalTo("RECEBIDA"))
            .body("criadoEm", notNullValue());
    }

    @Test
    public void deveRejeitarReclamacaoSemNome() {
        String requestBody = """
            {
                "email": "joao@teste.com",
                "descricao": "Produto com defeito"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/reclamacoes")
        .then()
            .statusCode(400);
    }

    @Test
    public void deveRejeitarReclamacaoSemEmail() {
        String requestBody = """
            {
                "nome": "João Silva",
                "descricao": "Produto com defeito"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/reclamacoes")
        .then()
            .statusCode(400);
    }

    @Test
    public void deveRejeitarReclamacaoSemDescricao() {
        String requestBody = """
            {
                "nome": "João Silva",
                "email": "joao@teste.com"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/reclamacoes")
        .then()
            .statusCode(400);
    }

    @Test
    public void deveRejeitarEmailInvalido() {
        String requestBody = """
            {
                "nome": "João Silva",
                "email": "email-invalido",
                "descricao": "Produto com defeito"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/reclamacoes")
        .then()
            .statusCode(400);
    }

    @Test
    public void deveGerarProtocoloUnicoParaCadaReclamacao() {
        String requestBody = """
            {
                "nome": "Maria Santos",
                "email": "maria@teste.com",
                "descricao": "Entrega atrasada"
            }
            """;

        String protocolo1 = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/reclamacoes")
        .then()
            .statusCode(201)
            .extract()
            .path("protocolo");

        String protocolo2 = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/reclamacoes")
        .then()
            .statusCode(201)
            .extract()
            .path("protocolo");

         Assertions.assertNotEquals(protocolo1, protocolo2, "Protocolos devem ser únicos");

    }

    @Test
    public void deveBuscarReclamacaoPorProtocoloComSucesso() {
        // Arrange - Criar uma reclamação primeiro
        String requestBody = """
            {
                "nome": "Paulo Mendes",
                "email": "paulo@teste.com",
                "descricao": "Produto com problema"
            }
            """;

        String protocolo = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/reclamacoes")
        .then()
            .statusCode(201)
            .extract()
            .path("protocolo");

        // Act & Assert
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/reclamacoes/" + protocolo)
        .then()
            .statusCode(200)
            .body("protocolo", equalTo(protocolo))
            .body("descricao", equalTo("Produto com problema"))
            .body("classificada", equalTo(true))
            .body("statusAtendimento", equalTo("RECEBIDA"))
            .body("ultimaAtualizacao", notNullValue())
            .body("categoriasPorDepartamento", notNullValue());
    }

    @Test
    public void deveRetornar404QuandoProtocoloNaoExiste() {
        String protocoloInexistente = "00000000-0000-0000-0000-000000000000";

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/reclamacoes/" + protocoloInexistente)
        .then()
            .statusCode(404);
    }

    @Test
    public void deveRetornarReclamacaoNaoClassificadaQuandoPendenteTriagemHumana() {
        // Arrange - Simular falha na classificação
        when(aiService.classificar(anyString())).thenReturn(new ClassificacaoResponse());

        String requestBody = """
            {
                "nome": "Roberto Lima",
                "email": "roberto@teste.com",
                "descricao": "Texto ambíguo sem contexto"
            }
            """;

        String protocolo = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/reclamacoes")
        .then()
            .statusCode(201)
            .extract()
            .path("protocolo");

        // Act & Assert
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/reclamacoes/" + protocolo)
        .then()
            .statusCode(200)
            .body("protocolo", equalTo(protocolo))
            .body("classificada", equalTo(false))
            .body("statusAtendimento", equalTo("PENDENTE_TRIAGEM_HUMANA"));
    }

    @Test
    public void deveRetornarMultiplasClassificacoes() {
        // Arrange - Simular múltiplas classificações
        ClassificacaoItem item1 = new ClassificacaoItem();
        item1.setDepartamento(Taxonomia.Departamento.LOGISTICA);
        item1.setCategoria(Taxonomia.Categoria.ATRASO_ENTREGA);
        item1.setMotivoExtraido("Atraso na entrega");

        ClassificacaoItem item2 = new ClassificacaoItem();
        item2.setDepartamento(Taxonomia.Departamento.PRODUTO_TECNICO);
        item2.setCategoria(Taxonomia.Categoria.PRODUTO_COM_DEFEITO);
        item2.setMotivoExtraido("Produto com defeito");

        ClassificacaoResponse response = new ClassificacaoResponse();
        response.setClassificacoes(List.of(item1, item2));
        when(aiService.classificar(anyString())).thenReturn(response);

        String requestBody = """
            {
                "nome": "Fernanda Costa",
                "email": "fernanda@teste.com",
                "descricao": "Produto chegou atrasado e com defeito"
            }
            """;

        String protocolo = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/reclamacoes")
        .then()
            .statusCode(201)
            .extract()
            .path("protocolo");

        // Act & Assert
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/reclamacoes/" + protocolo)
        .then()
            .statusCode(200)
            .body("protocolo", equalTo(protocolo))
            .body("classificada", equalTo(true))
            .body("categoriasPorDepartamento.size()", equalTo(2));
    }

    @Test
    public void deveRetornar400QuandoProtocoloInvalido() {
        String protocoloInvalido = "protocolo-invalido";

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/reclamacoes/" + protocoloInvalido)
        .then()
            .statusCode(400);
    }
}