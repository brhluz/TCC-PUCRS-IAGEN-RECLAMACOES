package br.com.pucrs.tcc.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class ReclamacaoResourceTest {

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
}