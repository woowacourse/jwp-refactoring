package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDateTime;
import java.util.Arrays;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrdersTest {

    @Test
    @DisplayName("Order들이 모두 Completion 상태인지 체크 테스트")
    public void validateCompletedTest_WithCookingStatus() throws Exception {
        //given
        Order order1 = Order.create(1L, OrderTable.create(2, false), OrderStatus.COMPLETION,
                LocalDateTime.now());
        Order order2 = Order.create(2L, OrderTable.create(4, false), OrderStatus.COMPLETION,
                LocalDateTime.now());
        Order order3 = Order.create(3L, OrderTable.create(5, false), OrderStatus.COOKING,
                LocalDateTime.now());
        Orders orders = Orders.create(Arrays.asList(order1, order2, order3));

        //then
        assertThatIllegalArgumentException().isThrownBy(orders::validateCompleted)
                .withMessage("orderId : " + order3.getId() + "인 Order가 Completion 상태가 아닙니다.");
    }
}
