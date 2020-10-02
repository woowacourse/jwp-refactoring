package kitchenpos.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TableGroupRestControllerIntegrationTest extends IntegrationTest {

    @DisplayName("테이블 그룹 생성")
    @Test
    void create() {
        Map<String, Object> data = new HashMap<>();
        data.put("orderTables", Arrays.asList(
            new HashMap<String, String>() {{
                put("id", "3");
            }},
            new HashMap<String, String>() {{
                put("id", "4");
            }})
        );

        // @formatter:off
        given().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(data).
        when().
            post("/api/table-groups").
        then().
            assertThat().
            statusCode(HttpStatus.CREATED.value()).
            header("Location", containsString("/api/table-groups/")).
            body("id", any(Integer.class)).
            body("createdDate", any(String.class)).
            body("orderTables", hasSize(2));
        // @formatter:on
    }


    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() {
        int tableGroupId = 1;
        // @formatter:off
        given().
        when().
            delete("/api/table-groups/" + tableGroupId).
        then().
            assertThat().
            statusCode(HttpStatus.NO_CONTENT.value());
        // @formatter:on
    }
}
