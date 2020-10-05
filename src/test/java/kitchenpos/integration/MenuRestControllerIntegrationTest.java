package kitchenpos.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuRestControllerIntegrationTest extends IntegrationTest {

    @DisplayName("메뉴 생성")
    @Test
    void create() {
        Map<String, Object> data = new HashMap<>();
        Map<String, String> menuProducts = new HashMap<>();
        menuProducts.put("productId", "1");
        menuProducts.put("quantity", "2");
        data.put("name", "후라이드+후라이드");
        data.put("price", "19000");
        data.put("menuGroupId", "1");
        data.put("menuProducts", Collections.singletonList(menuProducts));

        // @formatter:off
        given().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(data).
        when().
            post("/api/menus").
        then().
            assertThat().
            statusCode(HttpStatus.CREATED.value()).
            header("Location", containsString("/api/menus/")).
            body("name", equalTo("후라이드+후라이드")).
            body("price", equalTo(19000F)).
            body("menuGroupId", equalTo(1)).
            body("menuProducts", hasSize(1));
        // @formatter:on
    }

    @DisplayName("메뉴 전체 조회")
    @Test
    void list() {
        // @formatter:off
        given().
        when().
            get("/api/menus").
        then().
            assertThat().
            statusCode(HttpStatus.OK.value()).
            body("$", hasSize(greaterThan(0)));
        // @formatter:on
    }
}
