package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {

    @Test
    @DisplayName("Order 생성 성공 테스트")
    public void createTest() throws Exception {
        //given
        Long id = 1L;
        OrderTable orderTable = OrderTable.create(2, false);
        OrderStatus cooking = OrderStatus.COOKING;
        LocalDateTime now = LocalDateTime.now();

        //when
        Order actual = Order.create(id, orderTable, cooking, now);

        //then
        assertEquals(actual.getId(), id);
        assertEquals(actual.getOrderStatus(), cooking.name());
        assertEquals(actual.getOrderedTime(), now);
    }

    @Test
    @DisplayName("Order 생성 시 OrderTable이 비어 있으면 예외 테스트")
    public void validateEmptyOrderTableTest() throws Exception {
        //given
        Long id = 1L;
        OrderTable orderTable = OrderTable.create(1L, 2, true);
        OrderStatus cooking = OrderStatus.COOKING;
        LocalDateTime now = LocalDateTime.now();

        //then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            Order.create(id, orderTable, cooking, now);
        }).withMessage(id + " 테이블은 비어있어 주문할 수 없습니다.");
    }
}
