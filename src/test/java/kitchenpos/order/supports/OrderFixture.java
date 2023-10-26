package kitchenpos.order.supports;

import java.time.LocalDateTime;
import kitchenpos.order.domain.model.Order;
import kitchenpos.order.domain.model.OrderStatus;
import kitchenpos.table.domain.model.OrderTable;
import kitchenpos.table.supports.OrderTableFixture;

public class OrderFixture {

    private Long id = null;
    private OrderTable orderTable = OrderTableFixture.fixture().build();
    private OrderStatus orderStatus = OrderStatus.COOKING;
    private LocalDateTime orderedTime = LocalDateTime.of(2023, 10, 2, 10, 0);

    private OrderFixture() {
    }

    public static OrderFixture fixture() {
        return new OrderFixture();
    }

    public OrderFixture id(Long id) {
        this.id = id;
        return this;
    }

    public OrderFixture orderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
        return this;
    }

    public OrderFixture orderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }


    public OrderFixture orderedTime(LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
        return this;
    }

    public Order build() {
        return new Order(id, orderTable, orderStatus);
    }
}
