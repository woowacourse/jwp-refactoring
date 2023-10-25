package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 주문_테이블이_빈_테이블_일경우_예외가_발생한다() {
        OrderTable orderTable = new OrderTable(0, true);

        assertThatThrownBy(() -> new Order(orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문을 등록할 수 없는 빈 테이블 입니다.");
    }

    @Test
    void 주문_테이블이_null_일경우_예외가_발생한다() {
        assertThatThrownBy(() -> new Order(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문을 등록할 수 없는 빈 테이블 입니다.");
    }

    @Test
    void 주문을_생성할_수_있다() {
        OrderTable orderTable = new OrderTable(0, true);
        orderTable.setEmpty(false);
        Assertions.assertDoesNotThrow(() -> new Order(orderTable));
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        OrderTable orderTable = new OrderTable(0, true);
        orderTable.setEmpty(false);
        Order order = new Order(orderTable);
        OrderLineItem orderLineItem = new OrderLineItem(
                order,
                new Menu("로제떡볶이", Price.of(10000), new MenuGroup("분식")),
                2
        );

        order.setOrderStatus(OrderStatus.COMPLETION);

        org.assertj.core.api.Assertions.assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }
}
