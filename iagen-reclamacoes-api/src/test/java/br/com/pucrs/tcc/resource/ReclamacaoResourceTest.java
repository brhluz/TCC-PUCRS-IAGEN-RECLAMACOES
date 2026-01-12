package br.com.pucrs.tcc.resource; // Ajustado: Removido o prefixo 'src.test.java'

import io.quarkus.test.junit.QuarkusTest; // Adicionado: Necessário para o @QuarkusTest
import io.restassured.http.ContentType; // Ajustado: Corrigido de 'jdk.jfr.ContentType' para o do REST-assured
import org.junit.jupiter.api.Test; // Adicionado: Necessário para o @Test

import static io.restassured.RestAssured.given; // Adicionado: Import estático para o DSL do REST-assured

@QuarkusTest
public class ReclamacaoResourceTest {

    @Test
    public void testSubmitReclamacao() {
        String body = """
            {
                "nomeCliente": "João Silva",
                "emailCliente": "joao@email.com",
                "texto": "O produto veio com defeito e ninguém me atende."
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post("/reclamacoes")
                .then()
                .statusCode(201);
    }
}