package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;

class TableAcceptanceTest extends AcceptanceTest {

    @Test
    void create() {
        OrderTable orderTable = new OrderTable(1, false);
        OrderTable created = makeResponse("/api/tables", TestMethod.POST, orderTable)
            .as(OrderTable.class);

        assertThat(created.getId()).isNotNull();
    }

    @Test
    void list() {
        OrderTable orderTable1 = new OrderTable(1, false);
        OrderTable orderTable2 = new OrderTable(1, false);
        makeResponse("/api/tables", TestMethod.POST, orderTable1);
        makeResponse("/api/tables", TestMethod.POST, orderTable2);

        List<OrderTable> orderTables = makeResponse("/api/tables", TestMethod.GET).jsonPath()
            .getList(".", OrderTable.class);

        assertThat(orderTables.size()).isEqualTo(2);
    }

    @Test
    void changeEmpty() {
        Order order = makeResponse("/api/orders", TestMethod.POST, order()).as(Order.class);
        order.changeStatus(OrderStatus.COMPLETION);
        makeResponse("/api/orders/" + order.getId() + "/order-status",
            TestMethod.PUT, order).as(Order.class);

        OrderTable changed = makeResponse("/api/tables/" + order.getOrderTableId() + "/empty",
            TestMethod.PUT, OrderTable.EMPTY_TABLE).as(OrderTable.class);

        assertThat(changed.isEmpty()).isTrue();
    }

    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable(1, false);
        OrderTable created = makeResponse("/api/tables", TestMethod.POST, orderTable)
            .as(OrderTable.class);

        created.setNumberOfGuests(3);
        OrderTable changed = makeResponse(
            "/api/tables/" + String.valueOf(created.getId()) + "/number-of-guests", TestMethod.PUT,
            created)
            .as(OrderTable.class);

        assertThat(changed.getNumberOfGuests()).isEqualTo(created.getNumberOfGuests());
    }
}