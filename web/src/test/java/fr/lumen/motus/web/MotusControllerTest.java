package fr.lumen.motus.web;

import fr.lumen.motus.DictionaryUtils;
import io.quarkus.arc.AlternativePriority;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
public class MotusControllerTest {

    @AlternativePriority(0)
    @Dictionary
    public List<String> dictionary() {
        return DictionaryUtils.clean(Stream.of("CHAT", "CHIEN"));
    }

    @Test
    public void testSolve() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"first\": \"C\", \"length\":4}")
                .accept(ContentType.JSON)
                .when()
                .post("/solve")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("solution", equalTo(true))
                .body("word", equalTo("CHAT"));
    }

    @Test
    public void testMatch() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"answer\": \"ChAt\",\"propositions\": [\"Crue\",\"ABFE\",\"feas\"]}")
                .accept(ContentType.JSON)
                .when()
                .post("/match")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(equalTo("[\"RBBB\",\"JBBB\",\"BBRB\"]"));
    }

}
