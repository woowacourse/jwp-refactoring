package kitchenpos.acceptance;

import io.restassured.response.ValidatableResponse;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.support.RequestBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("테이블 관련 api")
public class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("테이블을 등록한다.")
    @Test
    void create() {
        // given, when
        final OrderTableRequest request = RequestBuilder.ofEmptyTable();
        final ValidatableResponse response = post("/api/tables", request);

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

        // when
        final OrderTableRequest request = RequestBuilder.ofEmptyTable();
        final ValidatableResponse response = put("/api/tables/" + savedTable.getId() + "/empty", request);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("empty", Matchers.is(true));
    }

    @DisplayName("테이블의 고객 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable savedTable = dataSupport.saveOrderTable(0, false);
        final int numberOfGuests = 2;

        // when
        final OrderTableRequest request = RequestBuilder.ofTableWithGuests(numberOfGuests);
        final ValidatableResponse response = put("/api/tables/" + savedTable.getId() + "/number-of-guests", request);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("numberOfGuests", Matchers.is(numberOfGuests));
    }
}
