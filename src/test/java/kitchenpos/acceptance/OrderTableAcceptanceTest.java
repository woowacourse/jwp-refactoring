package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import kitchenpos.acceptance.common.OrderTableHttpCommunication;
import kitchenpos.acceptance.common.fixture.RequestBody;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("OrderTableAcceptance 는 ")
public class OrderTableAcceptanceTest extends AcceptanceTest {

    @DisplayName("OrderTable 를 생성한다.")
    @Test
    void createOrderTable() {
        final ExtractableResponse<Response> response = OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_1)
                .getResponse();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("OrderTable 들을 가져온다.")
    @Test
    void getOrderTables() {
        OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_1);

        final List<OrderTable> orderTables = OrderTableHttpCommunication.getOrderTables()
                .getResponseBodyAsList(OrderTable.class);

        assertThat(orderTables.size()).isEqualTo(1);
    }

    @DisplayName("OrderTable 에 게스트 존재여부를 변경한다.")
    @Test
    void changeEmpty() {
        final OrderTable orderTable = OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_1)
                .getResponseBodyAsObject(OrderTable.class);

        final OrderTable result = OrderTableHttpCommunication.changeEmpty(orderTable.getId(),
                        RequestBody.NON_EMPTY_TABLE)
                .getResponseBodyAsObject(OrderTable.class);

        assertThat(result.isEmpty()).isFalse();
    }

    @DisplayName("OrderTable 에 있는 게스트 명수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        final OrderTable orderTable = OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_1)
                .getResponseBodyAsObject(OrderTable.class);
        final OrderTable nonEmptyOrderTable = OrderTableHttpCommunication.changeEmpty(orderTable.getId(),
                        RequestBody.NON_EMPTY_TABLE)
                .getResponseBodyAsObject(OrderTable.class);
        final Map<String, Object> requestBody = Map.of(
                "numberOfGuests", 3,
                "empty", nonEmptyOrderTable.isEmpty());
        final OrderTable result = OrderTableHttpCommunication.changeNumberOfGuests(orderTable.getId(), requestBody)
                .getResponseBodyAsObject(OrderTable.class);

        assertThat(result.getNumberOfGuests()).isEqualTo(3);
    }
}
