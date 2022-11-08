package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.util.List;
import java.util.Map;
import kitchenpos.table.application.dto.request.OrderTableRequest;
import kitchenpos.table.application.dto.response.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

public class TableAcceptanceTest extends AcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    public static Long createTable(OrderTableRequest table) {
        String location = RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(table)
                .when().log().all()
                .post("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().header("Location");

        return Long.parseLong(location.split("/api/tables/")[1]);
    }

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void findTables() {
        long tableId1 = createTable(new OrderTableRequest(0, true));
        long tableId2 = createTable(new OrderTableRequest(2, false));
        long tableId3 = createTable(new OrderTableRequest(3, false));

        List<OrderTableResponse> tables = getTables();

        assertThat(tables).extracting(OrderTableResponse::getId, OrderTableResponse::getNumberOfGuests,
                        OrderTableResponse::isEmpty)
                .containsExactlyInAnyOrder(
                        tuple(tableId1, 0, true),
                        tuple(tableId2, 2, false),
                        tuple(tableId3, 3, false)
                );
    }

    private List<OrderTableResponse> getTables() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList(".", OrderTableResponse.class);
    }

    @DisplayName("테이블의 주문 가능 여부 (empty)를 변경한다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeEmpty(boolean empty) {
        Long tableId = createTable(new OrderTableRequest(2, true));

        updateTableEmpty(tableId, empty);

        List<OrderTableResponse> tables = getTables();
        OrderTableResponse tableResponse = tables.stream()
                .filter(t -> tableId.equals(t.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(tableResponse.isEmpty()).isEqualTo(empty);
    }

    private void updateTableEmpty(long tableId, boolean empty) {
        RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(Map.of("empty", empty))
                .when().log().all()
                .put("/api/tables/" + tableId + "/empty")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("테이블의 방문한 손님 수를 변경한다.")
    @ParameterizedTest
    @ValueSource(ints = {4, 2})
    void changNumberOfGuests(int numberOfGuests) {
        Long tableId = createTable(new OrderTableRequest(7, false));

        updateTableNumberOfGuests(tableId, numberOfGuests);

        List<OrderTableResponse> tables = getTables();
        OrderTableResponse table = tables.stream()
                .filter(t -> tableId.equals(t.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(table.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    private void updateTableNumberOfGuests(Long tableId, int numberOfGuests) {
        RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(Map.of("numberOfGuests", numberOfGuests))
                .when().log().all()
                .put("/api/tables/" + tableId + "/number-of-guests")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
