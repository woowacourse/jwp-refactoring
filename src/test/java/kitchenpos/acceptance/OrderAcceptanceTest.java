package kitchenpos.acceptance;

import static kitchenpos.fixture.MenuFixture.MENU_1;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE_1;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.entity.OrderLineItem;
import kitchenpos.ui.jpa.dto.order.ChangeOrderStatusRequest;
import kitchenpos.ui.jpa.dto.order.OrderCreateRequest;
import kitchenpos.ui.jpa.dto.ordertable.ChangeEmptyRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class OrderAcceptanceTest extends AcceptanceTest {

    private static final String API = "/api/orders";

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void createOrder() {
        ChangeEmptyRequest changeEmptyRequest = new ChangeEmptyRequest(false);
        HttpMethodFixture.httpPut(changeEmptyRequest, "/api/tables/" + ORDER_TABLE_1.getId() + "/empty");

        OrderLineItem orderLineItem = new OrderLineItem(MENU_1.getId(), MENU_1.getPrice().getValue(), 1);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ORDER_TABLE_1.getId(), List.of(orderLineItem));

        ExtractableResponse<Response> response = HttpMethodFixture.httpPost(orderCreateRequest, API);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("주문을 모두 조회할 수 있다.")
    @Test
    void listOrder() {
        ExtractableResponse<Response> response = HttpMethodFixture.httpGet(API);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("주문의 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        ChangeEmptyRequest changeEmptyRequest = new ChangeEmptyRequest(false);
        HttpMethodFixture.httpPut(changeEmptyRequest, "/api/tables/" + ORDER_TABLE_1.getId() + "/empty");

        OrderLineItem orderLineItem = new OrderLineItem(MENU_1.getId(), MENU_1.getPrice().getValue(), 1);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ORDER_TABLE_1.getId(), List.of(orderLineItem));
        ExtractableResponse<Response> orderCreateResponse = HttpMethodFixture.httpPost(orderCreateRequest, API);

        Long orderId = orderCreateResponse.body().jsonPath().getObject("id", Long.class);
        ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest("MEAL");

        String api = API + "/" + orderId + "/order-status";
        ExtractableResponse<Response> response = HttpMethodFixture.httpPut(changeOrderStatusRequest, api);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
