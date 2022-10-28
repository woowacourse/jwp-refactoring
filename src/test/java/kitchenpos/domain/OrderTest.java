package kitchenpos.domain;

import static kitchenpos.fixture.OrderTableFixture.generateOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {

    @DisplayName("주문의 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        Order order = new Order(1L, 1L, "COOKING", LocalDateTime.now());

        // when
        order.changeOrderStatus("MEAL");

        // then
        assertThat(order.getOrderStatus()).isEqualTo("MEAL");
    }

    @DisplayName("주문의 상태를 변경 시 완료된 주문을 변경하면 예외를 반환한다.")
    @Test
    void changeOrderStatus_WhenCompletionOrder() {
        // given
        Order order = new Order(1L, 1L, "COMPLETION", LocalDateTime.now());

        // when & then
        assertThatThrownBy(() -> order.changeOrderStatus("MEAL"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료된 주문은 상태를 변경할 수 없습니다.");
    }

    @DisplayName("주문의 상태를 변경 시 완료된 주문을 변경하면 예외를 반환한다.")
    @Test
    void create_WhenEmptyOrderTable() {
        // given
        OrderTable orderTable = generateOrderTable(1L, 0, true);

        // when & then
        assertThatThrownBy(() -> Order.from(orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 생성 시 주문 받을 수 없는 주문 테이블일 수 없습니다.");
    }
}
