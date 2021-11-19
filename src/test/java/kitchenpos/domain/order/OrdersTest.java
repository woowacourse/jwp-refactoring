package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.Orders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrdersTest {

    private OrderTable orderTable;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(null, 0, true);
        orderTable2 = new OrderTable(null, 0, true);
    }

    @DisplayName("주문 가능 상태 변경 검증 성공")
    @Test
    void validateChangeStatus() {
        Order order = new Order(orderTable, OrderStatus.COMPLETION.name());
        Order order2 = new Order(orderTable2, OrderStatus.COMPLETION.name());
        Orders orders = new Orders(Arrays.asList(order, order2));
        assertThatCode(orders::validateChangeEmpty)
                .doesNotThrowAnyException();
    }

    @DisplayName("주문 가능 상태 변경 검증 실패")
    @Test
    void validateChangeStatusFail() {
        Order order = new Order(orderTable, OrderStatus.COOKING.name());
        Order order2 = new Order(orderTable2, OrderStatus.MEAL.name());
        Orders orders = new Orders(Arrays.asList(order, order2));
        assertThatThrownBy(orders::validateChangeEmpty)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
