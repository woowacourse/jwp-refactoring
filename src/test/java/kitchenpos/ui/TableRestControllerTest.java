package kitchenpos.ui;

import io.restassured.response.Response;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;

class TableRestControllerTest extends AcceptanceTest {
    @Test
    @DisplayName("테이블 목록을 조회한다.")
    void list() {
        get("/api/tables").then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", hasItems(1, 2, 3, 4, 5, 6, 7, 8));
    }

    @Test
    @DisplayName("테이블을 생성한다.")
    void create() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, false);

        // when
        Response response = post("/api/tables", orderTableRequest);

        // then
        response.then().assertThat().statusCode(HttpStatus.CREATED.value());

        OrderTableResponse orderTableResponse = response.then().extract().as(OrderTableResponse.class);
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
        assertThat(orderTableResponse.isEmpty()).isEqualTo(orderTableRequest.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({"true", "false"})
    @DisplayName("테이블의 주문 가능 상태를 변경한다.")
    void changeEmpty(Boolean isEmpty) {
        // given
        Long tableId = createTable();
        OrderTableRequest orderTableChangeEmptyRequest = new OrderTableRequest(isEmpty);

        // when
        Response response = put("/api/tables/" + tableId + "/empty", orderTableChangeEmptyRequest);

        // then
        response.then().statusCode(HttpStatus.OK.value());

        OrderTableResponse orderTableResponse = response.then().extract().as(OrderTableResponse.class);
        assertThat(orderTableResponse.isEmpty()).isEqualTo(isEmpty);
    }

    @Test
    @DisplayName("테이블의 방문한 손님을 변경한다.")
    void changeNumberOfGuests() {
        // given
        Long tableId = createTable();

        int numberOfGuests = new Random().nextInt(Integer.MAX_VALUE);
        OrderTableRequest orderTableChangeEmptyRequest = new OrderTableRequest(numberOfGuests);

        // when
        Response response = put("/api/tables/" + tableId + "/number-of-guests", orderTableChangeEmptyRequest);

        // then
        response.then().statusCode(HttpStatus.OK.value());

        OrderTableResponse orderTableResponse = response.then().extract().as(OrderTableResponse.class);
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    private Long createTable() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, false);
        return post("/api/tables", orderTableRequest)
                .then()
                .extract()
                .as(OrderTableResponse.class)
                .getId();
    }
}
