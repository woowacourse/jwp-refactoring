package kitchenpos.acceptance;

import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_1;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.ui.jpa.dto.ordertable.ChangeEmptyRequest;
import kitchenpos.ui.jpa.dto.ordertable.ChangeNumberOfGuestsRequest;
import kitchenpos.ui.jpa.dto.ordertable.OrderTableCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class OrderTableAcceptanceTest extends AcceptanceTest {

    private static final String API = "/api/tables";

    @DisplayName("테이블을 생성할 수 있다.")
    @Test
    void createOrderTable() {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(10, true);

        ExtractableResponse<Response> response = HttpMethodFixture.httpPost(orderTableCreateRequest, API);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("테이블을 모두 조회할 수 있다.")
    @Test
    void listOrderTable() {
        ExtractableResponse<Response> response = HttpMethodFixture.httpGet(API);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("테이블 empty 상태를 바꿀 수 있다.")
    @Test
    void changeEmpty() {
        ChangeEmptyRequest changeEmptyRequest = new ChangeEmptyRequest(false);

        String api = API + "/" + ORDER_TABLE_1.getId() + "/empty";
        ExtractableResponse<Response> response = HttpMethodFixture.httpPut(changeEmptyRequest, api);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("테이블 인원을 바꿀 수 있다.")
    @Test
    void changeNumberOfGuests() {
        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(11);

        String api = API + "/" + ORDER_TABLE_1.getId() + "/number-of-guests";
        ExtractableResponse<Response> response = HttpMethodFixture.httpPut(changeNumberOfGuestsRequest, api);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
