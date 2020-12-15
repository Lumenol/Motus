package fr.lumen.motus.web;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
public class SolverTest {

    @Test
    public void testSolve() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"first\": \"A\", \"length\":4}")
                .accept(ContentType.JSON)
                .when()
                .post("/solve")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("solution", equalTo(true))
                .body("word", equalTo("AAAA"));
    }

}
