package edu.innotech;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.matchesPattern;


public class StudentApiTest {

    private final static String BASE_URL = "http://localhost:8080/";

    @Test
    public void createStudentWithId() {
        String bodyForCreate = "{" +
                "\"id\":1," +
                "\"name\":\"Ivan\"" +
                "}";
        RestAssured.given()
                .baseUri(BASE_URL + "student")
                .contentType(ContentType.JSON)
                .body(bodyForCreate)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body(Matchers.anything());
    }

    @Test
    public void createStudentWithoutId() {
        String bodyForCreate = "{" +
                "\"name\":\"Ivan\"" +
                "}";

        RestAssured.given()
                .baseUri(BASE_URL + "student")
                .contentType(ContentType.JSON)
                .body(bodyForCreate)
                .when()
                .post()
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body(matchesPattern("^\\d+$"));
    }
}
