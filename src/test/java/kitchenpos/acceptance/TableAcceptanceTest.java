package kitchenpos.acceptance;

import io.restassured.response.ValidatableResponse;
import kitchenpos.domain.OrderTable;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("테이블 관련 api")
public class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("테이블을 등록한다.")
    @Test
    void create() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        // when
        final ValidatableResponse response = post("/api/tables", orderTable);

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header("Location", Matchers.notNullValue());
    }

    @DisplayName("테이블의 목록을 조회한다.")
    @Test
    void list() {
        // given, when
        final ValidatableResponse response = get("/api/tables");

        // then
        response.statusCode(HttpStatus.OK.value());
    }

    @DisplayName("테이블이 비었는지 상태를 변경한다.")
    @Test
    void changeEmpty() {
        // given
        final OrderTable savedTable = dataSupport.saveOrderTable(0, false);
        final OrderTable emptyTable = new OrderTable();
        emptyTable.setEmpty(true);

        // when
        final ValidatableResponse response = put("/api/tables/" + savedTable.getId() + "/empty", emptyTable);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("empty", Matchers.is(true));
    }

    @DisplayName("테이블의 고객 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable savedTable = dataSupport.saveOrderTable(0, false);
        final OrderTable tableWith2Guests = new OrderTable();
        tableWith2Guests.setNumberOfGuests(2);

        // when
        final ValidatableResponse response = put("/api/tables/" + savedTable.getId() + "/number-of-guests", tableWith2Guests);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("numberOfGuests", Matchers.is(2));
    }
}
