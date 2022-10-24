package acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.util.List;
import kitchenpos.Application;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = Application.class
)
public class TableAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    public static long createTable(int numberOfGuests, boolean empty) {
        OrderTable table = givenTable(numberOfGuests, empty);
        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(table)
                .when().log().all()
                .post("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().jsonPath().getLong("id");
    }

    public static OrderTable givenTable(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void findTables() {
        long tableId1 = createTable(0, true);
        long tableId2 = createTable(2, false);
        long tableId3 = createTable(3, false);

        List<OrderTable> tables = getTables();

        assertThat(tables).extracting(OrderTable::getId, OrderTable::getNumberOfGuests, OrderTable::isEmpty)
                .containsExactlyInAnyOrder(
                        tuple(tableId1, 0, true),
                        tuple(tableId2, 2, false),
                        tuple(tableId3, 3, false)
                );
    }

    private List<OrderTable> getTables() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().body().jsonPath().getList(".", OrderTable.class);
    }

    @DisplayName("테이블의 주문 가능 여부 (empty)를 변경한다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeEmpty(boolean empty) {
        long tableId = createTable(2, true);

        Long updatedTableId = updateTableEmpty(tableId, empty);

        List<OrderTable> tables = getTables();
        OrderTable table = tables.stream()
                .filter(t -> updatedTableId.equals(t.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(table.isEmpty()).isEqualTo(empty);
    }

    private Long updateTableEmpty(long tableId, boolean empty) {
        OrderTable table = givenTable(0, empty);

        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(table)
                .when().log().all()
                .put("/api/tables/" + tableId + "/empty")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getLong("id");
    }

    @DisplayName("테이블의 방문한 손님 수를 변경한다.")
    @ParameterizedTest
    @ValueSource(ints = {4, 2})
    void changNumberOfGuests(int numberOfGuests) {
        Long tableId = createTable(7, false);

        Long updatedTableId = updateTableNumberOfGuests(tableId, numberOfGuests, false);

        List<OrderTable> tables = getTables();
        OrderTable table = tables.stream()
                .filter(t -> updatedTableId.equals(t.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(table.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    private Long updateTableNumberOfGuests(Long tableId, int numberOfGuests, boolean empty) {
        OrderTable table = givenTable(numberOfGuests, empty);

        return RestAssured.given().log().all()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .body(table)
                .when().log().all()
                .put("/api/tables/" + tableId + "/number-of-guests")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getLong("id");
    }
}
