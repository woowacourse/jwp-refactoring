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
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("OrderAcceptance 는 ")
public class OrderAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        final ExtractableResponse<Response> response = order().getResponse();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("주문 항목을 가져온다.")
    @Test
    void getOrders() {
        order();
        final List<Order> orders = OrderHttpCommunication.getOrders()
                .getResponseBodyAsList(Order.class);

        assertThat(orders.size()).isEqualTo(1);
    }

    @DisplayName("주문 상태를 변경한다")
    @Test
    void changeOrderStatus() {
        final Order order = order().getResponseBodyAsObject(Order.class);
        final Order result = OrderHttpCommunication.changeOrderStatus(order.getId(),
                        Map.of("orderStatus", "MEAL"))
                .getResponseBodyAsObject(Order.class);

        assertThat(result.getOrderStatus()).isEqualTo("MEAL");
    }

    private HttpCommunication order() {
        final Product product = ProductHttpCommunication.create(RequestBody.PRODUCT)
                .getResponseBodyAsObject(Product.class);
        final MenuGroup menuGroup = MenuGroupHttpCommunication.create(RequestBody.MENU_GROUP)
                .getResponseBodyAsObject(MenuGroup.class);
        final Menu menu = MenuHttpCommunication.create(
                        RequestBody.getMenuProductFixture(product.getId(), menuGroup.getId()))
                .getResponseBodyAsObject(Menu.class);
        final OrderTable orderTable = OrderTableHttpCommunication.create(RequestBody.ORDER_TABLE_1)
                .getResponseBodyAsObject(OrderTable.class);
        final OrderTable nonEmptyOrderTable = OrderTableHttpCommunication.changeEmpty(orderTable.getId(),
                        RequestBody.NON_EMPTY_TABLE)
                .getResponseBodyAsObject(OrderTable.class);

        return OrderHttpCommunication.create(RequestBody.getOrder(menu.getId(), nonEmptyOrderTable.getId()));
    }
}
