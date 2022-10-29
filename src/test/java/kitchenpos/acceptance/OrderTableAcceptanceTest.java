package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import kitchenpos.acceptance.common.httpcommunication.MenuGroupHttpCommunication;
import kitchenpos.acceptance.common.httpcommunication.MenuHttpCommunication;
import kitchenpos.acceptance.common.httpcommunication.OrderHttpCommunication;
import kitchenpos.acceptance.common.httpcommunication.OrderTableHttpCommunication;
import kitchenpos.acceptance.common.httpcommunication.ProductHttpCommunication;
import kitchenpos.common.fixture.RequestBody;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.dto.request.OrderLineItemReeust;
import kitchenpos.ui.dto.response.MenuGroupResponse;
import kitchenpos.ui.dto.response.MenuResponse;
import kitchenpos.ui.dto.response.OrderResponse;
import kitchenpos.ui.dto.response.OrderTableResponse;
import kitchenpos.ui.dto.response.ProductResponse;
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

        final List<OrderTableResponse> orderTables = OrderTableHttpCommunication.getOrderTables()
                .getResponseBodyAsList(OrderTableResponse.class);

        assertThat(orderTables.size()).isEqualTo(1);
    }

    @DisplayName("OrderTable 에 게스트 존재여부를 변경한다.")
    @Test
    void changeEmpty() {
        final OrderTableResponse orderTable = OrderTableHttpCommunication.create(RequestBody.NON_EMPTY_TABLE)
                .getResponseBodyAsObject(OrderTableResponse.class);

        final ProductResponse product = ProductHttpCommunication.create(RequestBody.PRODUCT)
                .getResponseBodyAsObject(ProductResponse.class);

        final MenuGroupResponse menuGroup = MenuGroupHttpCommunication.create(RequestBody.MENU_GROUP)
                .getResponseBodyAsObject(MenuGroupResponse.class);

        final MenuResponse menu = MenuHttpCommunication.create(
                        RequestBody.getMenuProductFixture(product.getId(), menuGroup.getId()))
                        .getResponseBodyAsObject(MenuResponse.class);

        final OrderResponse order = OrderHttpCommunication.create(
                        RequestBody.getOrder(menu.getId(), orderTable.getId()))
                .getResponseBodyAsObject(OrderResponse.class);

        OrderHttpCommunication.changeOrderStatus(order.getId(), Map.of("orderStatus", "COMPLETION"));
        final OrderTableResponse result = OrderTableHttpCommunication.changeEmpty(orderTable.getId(),
                        RequestBody.NON_EMPTY_TABLE)
                .getResponseBodyAsObject(OrderTableResponse.class);

        assertThat(result.isEmpty()).isFalse();
    }

    @DisplayName("OrderTable 에 있는 게스트 명수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        final OrderTableResponse orderTable = OrderTableHttpCommunication.create(RequestBody.NON_EMPTY_TABLE)
                .getResponseBodyAsObject(OrderTableResponse.class);

        final ProductResponse product = ProductHttpCommunication.create(RequestBody.PRODUCT)
                .getResponseBodyAsObject(ProductResponse.class);

        final MenuGroupResponse menuGroup = MenuGroupHttpCommunication.create(RequestBody.MENU_GROUP)
                .getResponseBodyAsObject(MenuGroupResponse.class);

        final MenuResponse menu = MenuHttpCommunication.create(
                        RequestBody.getMenuProductFixture(product.getId(), menuGroup.getId()))
                .getResponseBodyAsObject(MenuResponse.class);

        final OrderResponse order = OrderHttpCommunication.create(
                        RequestBody.getOrder(menu.getId(), orderTable.getId()))
                .getResponseBodyAsObject(OrderResponse.class);

        OrderHttpCommunication.changeOrderStatus(order.getId(), Map.of("orderStatus", "COMPLETION"));
        final OrderTableResponse nonEmptyOrderTable = OrderTableHttpCommunication.changeEmpty(orderTable.getId(),
                        RequestBody.NON_EMPTY_TABLE)
                .getResponseBodyAsObject(OrderTableResponse.class);

        final Map<String, Object> requestBody = Map.of(
                "numberOfGuests", 3,
                "empty", nonEmptyOrderTable.isEmpty());
        final OrderTableResponse result = OrderTableHttpCommunication.changeNumberOfGuests(orderTable.getId(), requestBody)
                .getResponseBodyAsObject(OrderTableResponse.class);

        assertThat(result.getNumberOfGuests()).isEqualTo(3);
    }
}
