package acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import common.AcceptanceTest;
import io.restassured.RestAssured;
import java.util.List;
import java.util.Map;
import kitchenpos.ui.request.OrderTableRequest;
import kitchenpos.ui.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void findOrderTables() {
        // act
        List<OrderTableResponse> orderTables = getOrderTables();

        // assert
        assertThat(orderTables)
                .extracting(
                        OrderTableResponse::getId, OrderTableResponse::getNumberOfGuests,
                        OrderTableResponse::isEmpty, OrderTableResponse::getTableGroupId
                )
                .hasSize(8)
                .containsExactlyInAnyOrder(
                        tuple(1L, 0, true, null),
                        tuple(2L, 0, true, null),
                        tuple(3L, 0, true, null),
                        tuple(4L, 0, true, null),
                        tuple(5L, 0, true, null),
                        tuple(6L, 0, true, null),
                        tuple(7L, 0, true, null),
                        tuple(8L, 0, true, null)
                );
    }

    @DisplayName("빈 테이블을 주문 테이블로 변경한다.")
    @Test
    void changeOrderTableToEmptyFalse() {
        // act
        OrderTableResponse orderTable = changeTableEmptyStatus(1L, false);

        // assert
        assertThat(orderTable.getId()).isEqualTo(1L);
        assertThat(orderTable.isEmpty()).isFalse();

        List<OrderTableResponse> orderTables = getOrderTables();
        assertThat(orderTables)
                .extracting(
                        OrderTableResponse::getId, OrderTableResponse::getNumberOfGuests,
                        OrderTableResponse::isEmpty, OrderTableResponse::getTableGroupId
                )
                .containsExactlyInAnyOrder(
                        tuple(1L, 0, false, null),
                        tuple(2L, 0, true, null),
                        tuple(3L, 0, true, null),
                        tuple(4L, 0, true, null),
                        tuple(5L, 0, true, null),
                        tuple(6L, 0, true, null),
                        tuple(7L, 0, true, null),
                        tuple(8L, 0, true, null)
                );
    }

    @DisplayName("테이블의 방문 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // arrange
        changeTableEmptyStatus(1L, false);

        // act
        OrderTableResponse orderTable = changeTableNumberOfGuests(1L, 10);

        // assert
        assertThat(orderTable.getId()).isEqualTo(1L);
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);

        List<OrderTableResponse> orderTables = getOrderTables();
        assertThat(orderTables)
                .extracting(
                        OrderTableResponse::getId, OrderTableResponse::getNumberOfGuests,
                        OrderTableResponse::isEmpty, OrderTableResponse::getTableGroupId
                )
                .containsExactlyInAnyOrder(
                        tuple(1L, 10, false, null),
                        tuple(2L, 0, true, null),
                        tuple(3L, 0, true, null),
                        tuple(4L, 0, true, null),
                        tuple(5L, 0, true, null),
                        tuple(6L, 0, true, null),
                        tuple(7L, 0, true, null),
                        tuple(8L, 0, true, null)
                );
    }

    @DisplayName("테이블을 생성한다.")
    @Test
    void createTable() {
        // act
        OrderTableResponse orderTable = createOrderTable(10, false);

        // assert
        assertThat(orderTable.getId()).isNotNull();
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
        assertThat(orderTable.isEmpty()).isFalse();
        assertThat(orderTable.getTableGroupId()).isNull();

        List<OrderTableResponse> orderTables = getOrderTables();
        assertThat(orderTables)
                .extracting(
                        OrderTableResponse::getId, OrderTableResponse::getNumberOfGuests,
                        OrderTableResponse::isEmpty, OrderTableResponse::getTableGroupId
                )
                .hasSize(9)
                .contains(tuple(orderTable.getId(), 10, false, null));
    }

    private OrderTableResponse createOrderTable(final int numberOfGuests, final boolean isEmpty) {
        final OrderTableRequest request = new OrderTableRequest(numberOfGuests, isEmpty);

        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(request)
                .when().log().all()
                .post("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(OrderTableResponse.class);
    }

    private List<OrderTableResponse> getOrderTables() {
        return RestAssured.given().log().all()
                .when().log().all()
                .get("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList(".", OrderTableResponse.class);
    }

    private OrderTableResponse changeTableEmptyStatus(final long tableId, final boolean isEmpty) {
        final OrderTableRequest request = new OrderTableRequest(isEmpty);

        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .pathParam("table-id", tableId)
                .body(request)
                .when().log().all()
                .put("/api/tables/{table-id}/empty")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(OrderTableResponse.class);
    }

    private OrderTableResponse changeTableNumberOfGuests(long tableId, int numberOfGuests) {
        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .pathParam("table-id", tableId)
                .body(Map.of("numberOfGuests", numberOfGuests))
                .when().log().all()
                .put("/api/tables/{table-id}/number-of-guests")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(OrderTableResponse.class);
    }
}
