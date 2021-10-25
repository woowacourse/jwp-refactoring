package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.Test;

class OrderAcceptanceTest extends AcceptanceTest {

    @Test
    void create() {
        Order order = order();
        Order created = makeResponse("/api/orders", TestMethod.POST, order).as(Order.class);

        assertAll(
            () -> assertThat(created.getId()).isNotNull(),
            () -> assertThat(created.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
            () -> assertThat(created.getOrderLineItems()).isNotEmpty()
        );
    }

    @Test
    void list() {
        Order order = order();
        makeResponse("/api/orders", TestMethod.POST, order);

        List<Order> orders = makeResponse("/api/orders", TestMethod.GET).jsonPath()
            .getList(".", Order.class);

        assertThat(orders.size()).isEqualTo(1);
    }

    @Test
    void changeOrderStatus() {
        Order order = makeResponse("/api/orders", TestMethod.POST,
            order()).as(Order.class);
        order.changeStatus(OrderStatus.COMPLETION);
        Order changed = makeResponse("/api/orders/" + order.getId() + "/order-status",
            TestMethod.PUT, order).as(Order.class);

        assertThat(changed.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }
}