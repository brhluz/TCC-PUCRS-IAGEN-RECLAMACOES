package src.test.java.br.com.pucrs.tcc.resource;

import jdk.jfr.ContentType;

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
                .statusCode(201); // Vai falhar porque o endpoint não existe (RED)
    }
}