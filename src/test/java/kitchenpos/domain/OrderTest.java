package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {

    @Test
    @DisplayName("주문을 생성하면 조리 상태가 된다.")
    void create() {
        Order order = Order.create(new OrderTable(2, false), List.of(new OrderLineItem(1L, 2)));

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    @DisplayName("계산 완료 상태로 변경하면 예외가 발생한다.")
    void changeStatusIsCompletion() {
        Order order = Order.create(new OrderTable(2, false), List.of(new OrderLineItem(1L, 2)));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> order.changeStatus(OrderStatus.COMPLETION))
                .withMessage("계산 완료된 주문입니다.");
    }
}
