package kitchenpos.order.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {

    @Test
    @DisplayName("Order 생성 성공 테스트")
    public void createTest() throws Exception {
        //given
        Long id = 1L;
        Long orderTableId = 1L;
        OrderStatus cooking = OrderStatus.COOKING;
        LocalDateTime now = LocalDateTime.now();

        //when
        Order actual = Order.create(id, orderTableId, cooking, now);

        //then
        assertEquals(actual.getId(), id);
        assertEquals(actual.getOrderStatus(), cooking.name());
        assertEquals(actual.getOrderedTime(), now);
    }
}
