package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @Test
    @DisplayName("생성할 때 주문 테이블이 없으면 예외가 발생한다")
    void create() {
        assertThatThrownBy(() -> new Order(null, List.of(new OrderLineItem(1L, "떡볶이 세트", Price.valueOf(3000), 4))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 없거나 빈 주문 테이블입니다.");
    }

    @Test
    @DisplayName("생성할 때 주문 항목이 없으면 예외가 발생한다")
    void create2() {
        assertThatThrownBy(() -> new Order(1L, emptyList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 필요합니다.");
    }

    @Test
    @DisplayName("주문 상태를 변경할 때 완료 상태이면 예외가 발생한다")
    void changeOrderStatus() {
        //given
        final Order order = new Order(1L, 1L, OrderStatus.COMPLETION, LocalDateTime.now(),
                List.of(new OrderLineItem(1L, "떡볶이 세트", Price.valueOf(3000), 4)));

        //when, then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("완료된 주문은 변경할 수 없습니다.");
    }
}
