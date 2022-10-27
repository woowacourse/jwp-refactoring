package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문 테이블을 새로 생성할 수 있다.")
    @Test
    void create() {
        // given
        final OrderTable orderTable = new OrderTable(0, true);

        // when
        final OrderTable response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().log().all()
                .post("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(OrderTable.class);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(orderTable);
    }

    @DisplayName("전체 주문 테이블 목록을 볼 수 있다.")
    @Test
    void list() {
        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().log().all()
                .get("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        final List<OrderTable> orderTables = getOrderTables(response);

        // then
        assertThat(orderTables)
                .hasSize(8)
                .extracting(OrderTable::getNumberOfGuests, OrderTable::isEmpty)
                .containsExactlyInAnyOrder(
                        tuple(0, true), tuple(0, true),
                        tuple(0, true), tuple(0, true),
                        tuple(0, true), tuple(0, true),
                        tuple(0, true), tuple(0, true)
                );
    }

    @DisplayName("주문 테이블을 빈 상태로 변경할 수 있다.")
    @Test
    void changeEmpty() {
        // given
        final OrderTable orderTable = saveOrderTable(0, true);
        final OrderTable changeOrderTable = new OrderTable(1, false);

        // when
        final OrderTable response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .pathParam("orderTableId", orderTable.getId())
                .body(changeOrderTable)
                .when().log().all()
                .put("/api/tables/{orderTableId}/empty")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(OrderTable.class);

        // then
        assertThat(response.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = saveOrderTable(1, false);
        final OrderTable changeOrderTable = new OrderTable(2, false);

        // when
        final OrderTable response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .pathParam("orderTableId", orderTable.getId())
                .body(changeOrderTable)
                .when().log().all()
                .put("/api/tables/{orderTableId}/number-of-guests")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(OrderTable.class);

        // then
        assertThat(response.getNumberOfGuests()).isEqualTo(changeOrderTable.getNumberOfGuests());
    }

    private static OrderTable saveOrderTable(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().log().all()
                .post("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().as(OrderTable.class);
    }

    private static List<OrderTable> getOrderTables(final ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", OrderTable.class);
    }
}
