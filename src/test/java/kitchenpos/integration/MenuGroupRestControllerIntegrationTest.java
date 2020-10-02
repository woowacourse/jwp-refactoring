package kitchenpos.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuGroupRestControllerIntegrationTest extends IntegrationTest {

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        Map<String, String> data = new HashMap<>();
        data.put("name", "추천메뉴");

        // @formatter:off
        given().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(data).
        when().
            post("/api/menu-groups").
        then().
            assertThat().
            statusCode(HttpStatus.CREATED.value()).
            header("Location", containsString("/api/menu-groups/")).
            body("id", any(Integer.class)).
            body("name", equalTo("추천메뉴"));
        // @formatter:on
    }

    @DisplayName("메뉴 그룹 전체 조회")
    @Test
    void list() {
        // @formatter:off
        given().
        when().
            get("/api/menu-groups").
        then().
            assertThat().
            statusCode(HttpStatus.OK.value()).
            body("$", hasSize(greaterThan(0)));
        // @formatter:on
    }
}
