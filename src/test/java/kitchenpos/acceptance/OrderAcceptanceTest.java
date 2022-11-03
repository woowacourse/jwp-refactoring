package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import kitchenpos.acceptance.common.httpcommunication.HttpCommunication;
import kitchenpos.acceptance.common.httpcommunication.MenuGroupHttpCommunication;
import kitchenpos.acceptance.common.httpcommunication.MenuHttpCommunication;
import kitchenpos.acceptance.common.httpcommunication.OrderHttpCommunication;
import kitchenpos.acceptance.common.httpcommunication.OrderTableHttpCommunication;
import kitchenpos.acceptance.common.httpcommunication.ProductHttpCommunication;
import kitchenpos.common.fixture.RequestBody;
import kitchenpos.ui.dto.response.MenuGroupResponse;
import kitchenpos.ui.dto.response.MenuResponse;
import kitchenpos.ui.dto.response.OrderResponse;
import kitchenpos.ui.dto.response.OrderTableResponse;
import kitchenpos.ui.dto.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("OrderAcceptance 는 ")
public class OrderAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문 항목을 가져온다.")
    @Test
    void getOrders() {
        order();
        final List<OrderResponse> orders = OrderHttpCommunication.getOrders()
                .getResponseBodyAsList(OrderResponse.class);

        assertThat(orders.size()).isEqualTo(1);
    }

    private void order() {
        final ProductResponse product = ProductHttpCommunication.create(RequestBody.PRODUCT)
                .getResponseBodyAsObject(ProductResponse.class);
        final MenuGroupResponse menuGroup = MenuGroupHttpCommunication.create(RequestBody.MENU_GROUP)
                .getResponseBodyAsObject(MenuGroupResponse.class);
        final MenuResponse menu = MenuHttpCommunication.create(
                        RequestBody.getMenuProductFixture(product.getId(), menuGroup.getId()))
                .getResponseBodyAsObject(MenuResponse.class);
        final OrderTableResponse orderTable = OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_1)
                .getResponseBodyAsObject(OrderTableResponse.class);
        final OrderTableResponse nonEmptyOrderTable = OrderTableHttpCommunication.changeEmpty(orderTable.getId(),
                        RequestBody.NON_EMPTY_TABLE)
                .getResponseBodyAsObject(OrderTableResponse.class);

        OrderHttpCommunication.create(RequestBody.getOrder(menu.getId(), nonEmptyOrderTable.getId()));
    }

    @DisplayName("주문을 생성할 때 ")
    @Nested
    class OrderCreationTest extends AcceptanceTest {

        @DisplayName("생성 가능하면 주문을 생성한다.")
        @Test
        void createOrderSuccess() {
            final ExtractableResponse<Response> response = order().getResponse();

            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(response.header("Location")).isNotBlank();
        }

        @DisplayName("상품이 존재하지 않으면 에러응답이 온다.")
        @Test
        void createOrderFail() {
            final OrderTableResponse orderTable = OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_1)
                    .getResponseBodyAsObject(OrderTableResponse.class);
            final OrderTableResponse nonEmptyOrderTable = OrderTableHttpCommunication.changeEmpty(orderTable.getId(),
                            RequestBody.NON_EMPTY_TABLE)
                    .getResponseBodyAsObject(OrderTableResponse.class);

            final ExtractableResponse<Response> response = OrderHttpCommunication.create(
                            RequestBody.getOrder(2L, nonEmptyOrderTable.getId()))
                    .getResponse();

            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        private HttpCommunication order() {
            final ProductResponse product = ProductHttpCommunication.create(RequestBody.PRODUCT)
                    .getResponseBodyAsObject(ProductResponse.class);
            final MenuGroupResponse menuGroup = MenuGroupHttpCommunication.create(RequestBody.MENU_GROUP)
                    .getResponseBodyAsObject(MenuGroupResponse.class);
            final MenuResponse menu = MenuHttpCommunication.create(
                            RequestBody.getMenuProductFixture(product.getId(), menuGroup.getId()))
                    .getResponseBodyAsObject(MenuResponse.class);
            final OrderTableResponse orderTable = OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_1)
                    .getResponseBodyAsObject(OrderTableResponse.class);
            final OrderTableResponse nonEmptyOrderTable = OrderTableHttpCommunication.changeEmpty(orderTable.getId(),
                            RequestBody.NON_EMPTY_TABLE)
                    .getResponseBodyAsObject(OrderTableResponse.class);

            return OrderHttpCommunication.create(RequestBody.getOrder(menu.getId(), nonEmptyOrderTable.getId()));
        }
    }

    @DisplayName("주문 상태를 변경할 때 ")
    @Nested
    class OrderStatusAlternationTest extends AcceptanceTest {

        @DisplayName("주문 상태를 변경한다")
        @Test
        void changeOrderStatusSuccess() {
            final OrderResponse order = order().getResponseBodyAsObject(OrderResponse.class);
            final OrderResponse result = OrderHttpCommunication.changeOrderStatus(order.getId(),
                            Map.of("orderStatus", "MEAL"))
                    .getResponseBodyAsObject(OrderResponse.class);

            assertThat(result.getOrderStatus().name()).isEqualTo("MEAL");
        }

        @DisplayName("주문 상태가 COMPLETION이면 에러를 던진다.")
        @Test
        void changeOrderStatusFail() {
            final OrderResponse order = order().getResponseBodyAsObject(OrderResponse.class);
            OrderHttpCommunication.changeOrderStatus(order.getId(), Map.of("orderStatus", "COMPLETION"))
                    .getResponseBodyAsObject(OrderResponse.class);

            final ExtractableResponse<Response> response = OrderHttpCommunication.changeOrderStatus(order.getId(),
                            Map.of("orderStatus", "MEAL"))
                    .getResponse();

            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        private HttpCommunication order() {
            final ProductResponse product = ProductHttpCommunication.create(RequestBody.PRODUCT)
                    .getResponseBodyAsObject(ProductResponse.class);
            final MenuGroupResponse menuGroup = MenuGroupHttpCommunication.create(RequestBody.MENU_GROUP)
                    .getResponseBodyAsObject(MenuGroupResponse.class);
            final MenuResponse menu = MenuHttpCommunication.create(
                            RequestBody.getMenuProductFixture(product.getId(), menuGroup.getId()))
                    .getResponseBodyAsObject(MenuResponse.class);
            final OrderTableResponse orderTable = OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_1)
                    .getResponseBodyAsObject(OrderTableResponse.class);
            final OrderTableResponse nonEmptyOrderTable = OrderTableHttpCommunication.changeEmpty(orderTable.getId(),
                            RequestBody.NON_EMPTY_TABLE)
                    .getResponseBodyAsObject(OrderTableResponse.class);

            return OrderHttpCommunication.create(RequestBody.getOrder(menu.getId(), nonEmptyOrderTable.getId()));
        }
    }
}
