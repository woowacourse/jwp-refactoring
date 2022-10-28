package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 생성_시_주문항목이_빈_경우_예외가_발생한다() {
        assertThatThrownBy(() -> new Order(1L, new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문항목이 비어있습니다.");
    }

    @Test
    void 메뉴크기가_다른_경우_예외가_발생한다() {
        Order order = new Order(1L, Arrays.asList(new OrderLineItem(1L, 1)));
        assertThatThrownBy(() -> order.validateMenuSize(2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("실제 메뉴로만 주문이 가능합니다.");
    }

    @Test
    void 완료된_주문상태를_변경하는경우_예외가_발생한다() {
        Order order = new Order(1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(1L, 1)));
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료된 주문의 상태는 변경할 수 없습니다.");
    }

    @Test
    void 주문상태를_변경한다() {
        Order order = new Order(1L, Arrays.asList(new OrderLineItem(1L, 1)));
        String actual = order.changeOrderStatus(OrderStatus.COOKING);
        assertThat(actual).isEqualTo(OrderStatus.COOKING.name());
    }
}
