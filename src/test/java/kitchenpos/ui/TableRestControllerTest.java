package kitchenpos.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TableRestControllerTest extends ControllerTest {
    @Test
    @DisplayName("OrderTable 생성")
    void create() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        ExtractableResponse<Response> response = postOrderTable(orderTable);
        OrderTable savedOrderTable = response.as(OrderTable.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @Test
    @DisplayName("모든 OrderTable 조회")
    void list() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        postOrderTable(orderTable);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        postOrderTable(orderTable2);

        ExtractableResponse<Response> response = getOrderTables();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().as(List.class)).hasSize(2);
    }

    @Test
    @DisplayName("빈 테이블로 변경")
    void changeEmpty() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(3);
        OrderTable savedOrderTable = postOrderTable(orderTable).as(OrderTable.class);

        OrderTable changeTable = new OrderTable();
        changeTable.setEmpty(true);
        changeTable.setNumberOfGuests(3);

        ExtractableResponse<Response> response = putOrderTableEmpty(savedOrderTable.getId(), changeTable);
        OrderTable changedTable = response.as(OrderTable.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(changedTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블 손님 수 변경")
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(3);
        OrderTable savedOrderTable = postOrderTable(orderTable).as(OrderTable.class);

        OrderTable changeTable = new OrderTable();
        changeTable.setEmpty(false);
        changeTable.setNumberOfGuests(5);

        ExtractableResponse<Response> response = putOrderTableGuest(savedOrderTable.getId(), changeTable);
        OrderTable changedTable = response.as(OrderTable.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(changedTable.getNumberOfGuests()).isEqualTo(changedTable.getNumberOfGuests());
    }

    static ExtractableResponse<Response> postOrderTable(OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().post("/api/tables")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> getOrderTables() {
        return RestAssured
                .given().log().all()
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> putOrderTableEmpty(Long orderTableId, OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/" + orderTableId + "/empty")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> putOrderTableGuest(Long orderTableId, OrderTable orderTable) {
        return RestAssured
                .given().log().all()
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTable)
                .when().put("/api/tables/" + orderTableId + "/number-of-guests")
                .then().log().all().extract();
    }
}
