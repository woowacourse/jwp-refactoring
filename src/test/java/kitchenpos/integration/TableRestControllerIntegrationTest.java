package kitchenpos.integration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import java.util.HashMap;
import java.util.Map;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TableRestControllerIntegrationTest extends IntegrationTest {

    @Autowired
    private TableService tableService;

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
            body("numberOfGuests", equalTo(0)).
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
        OrderTable orderTable = tableService.create(new OrderTable());
        orderTable.setEmpty(true);
        OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), orderTable);

        Map<String, Object> data = new HashMap<>();
        data.put("empty", false);

        // @formatter:off
        given().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(data).
        when().
            put("/api/tables/" + changedOrderTable.getId() + "/empty").
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
        OrderTable orderTable = tableService.create(new OrderTable());

        Map<String, Object> data = new HashMap<>();
        data.put("numberOfGuests", 4);

        // @formatter:off
        given().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(data).
        when().
            put("/api/tables/" + orderTable.getId() + "/number-of-guests").
        then().
            assertThat().
            statusCode(HttpStatus.OK.value()).
            body("id", equalTo(Math.toIntExact(orderTable.getId()))).
            body("tableGroupId", equalTo(null)).
            body("numberOfGuests", equalTo(4)).
            body("empty", equalTo(false));
        // @formatter:on
    }
}
