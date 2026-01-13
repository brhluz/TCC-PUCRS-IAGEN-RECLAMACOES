package br.com.pucrs.tcc;

import io.quarkus.test.junit.QuarkusTest; // Adicionado: Necessário para o @QuarkusTest
import io.restassured.http.ContentType; // Ajustado: Corrigido de 'jdk.jfr.ContentType' para o do REST-assured
import org.junit.jupiter.api.Test; // Adicionado: Necessário para o @Test

import static io.restassured.RestAssured.given; // Adicionado: Import estático para o DSL do REST-assured

@QuarkusTest
public class ReclamacaoResourceTest {

}