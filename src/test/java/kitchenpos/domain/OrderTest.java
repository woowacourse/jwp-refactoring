package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문 테이블이 null 이고 주문을 생성할 경우 예외가 발생한다.")
    void createWithNullOrderTables() {
        assertThatThrownBy(() -> Order.of(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상품 목록이 없으면 주문을 생성할 수 없습니다.");
    }

    @Test
    @DisplayName("주문 테이블이 비어있고 주문을 생성할 경우 예외가 발생한다.")
    void createWithEmptyOrderTables() {
        assertThatThrownBy(() -> Order.of(1L, Arrays.asList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상품 목록이 없으면 주문을 생성할 수 없습니다.");
    }

    @Test
    @DisplayName("주문을 생성하면 COOKING 상태로 바뀌어야 한다.")
    void changeStatus() {
        final Order order = getOrder();
        assertThat(order.getOrderStatus()).isEqualTo(COOKING);
    }

    @Test
    @DisplayName("주문을 생성하면 현재시간이 주문시간으로 등록되어야 한다.")
    void createWithOrderTime() {
        final Order order = getOrder();
        assertThat(order.getOrderStatus()).isNotNull();
    }

    @Test
    @DisplayName("COMPLETION 상태에서는 주문 상태를 변경할 수 없다.")
    void changeInvalidOrderStatus() {
        final Order order = getOrder();
        order.changeOrderStatus(COMPLETION);
        assertThatThrownBy(() -> order.changeOrderStatus(COOKING))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("COMPLETION 상태에서는 주문 상태를 변경할 수 없습니다.");
    }

    private Order getOrder() {
        return Order.of(1L, Arrays.asList(new OrderLineItem(1L, 2)));
    }
}
