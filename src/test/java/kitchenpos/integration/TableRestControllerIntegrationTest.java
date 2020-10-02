package kitchenpos.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TableRestControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("테이블 생성")
    @Test
    void create() {
        Map<String, Object> data = new HashMap<>();
        data.put("numberOfGuests", 0);
        data.put("empty", true);

        // @formatter:off
        given().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(data).
        when().
            post("/api/tables").
        then().
            assertThat().
            statusCode(HttpStatus.CREATED.value()).
            header("Location", containsString("/api/tables/")).
            body("id", any(Integer.class)).
            body("tableGroupId", equalTo(null)).
            body("numberOfGuests", any(Integer.class)).
            body("empty", equalTo(true));
        // @formatter:on
    }

    @DisplayName("테이블 전체 조회")
    @Test
    void list() {
        // @formatter:off
        given().
        when().
            get("/api/tables").
        then().
            assertThat().
            statusCode(HttpStatus.OK.value()).
            body("$", hasSize(greaterThan(0)));
        // @formatter:on
    }

    @DisplayName("빈(Empty) 테이블 수정")
    @Test
    void changeEmpty() {
        int orderTableId = 1;
        Map<String, Object> data = new HashMap<>();
        data.put("empty", false);

        // @formatter:off
        given().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(data).
        when().
            put("/api/tables/" + orderTableId + "/empty").
        then().
            assertThat().
            statusCode(HttpStatus.OK.value()).
            body("id", any(Integer.class)).
            body("tableGroupId", equalTo(null)).
            body("numberOfGuests", any(Integer.class)).
            body("empty", equalTo(false));
        // @formatter:on
    }

    @DisplayName("테이블 게스트 수 수정")
    @Test
    void changeNumberOfGuests() {
        int orderTableId = 1;
        Map<String, Object> data = new HashMap<>();
        data.put("numberOfGuests", 4);

        // @formatter:off
        given().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(data).
        when().
            put("/api/tables/" + orderTableId + "/number-of-guests").
        then().
            assertThat().
            statusCode(HttpStatus.OK.value()).
            body("id", any(Integer.class)).
            body("tableGroupId", equalTo(null)).
            body("numberOfGuests", any(Integer.class)).
            body("empty", equalTo(false));
        // @formatter:on
    }
}
