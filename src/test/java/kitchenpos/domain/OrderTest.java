package kitchenpos.domain;

import static kitchenpos.domain.order.OrderStatus.COMPLETION;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.domain.order.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.fixtures.TestFixtures;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 정상적으로_주문을_생성한다() {
        // given
        final OrderTable orderTable = TestFixtures.주문_테이블_생성(null, 10, false);

        // when, then
        assertThatCode(() -> Order.of(orderTable, COOKING, LocalDateTime.now(), Collections.emptyList()))
                .doesNotThrowAnyException();
    }

    @Test
    void 주문_생성시_주문_테이블이_비어_있다면_예외가_발생한다() {
        // given
        final OrderTable orderTable = TestFixtures.주문_테이블_생성(null, 10, true);

        // when, then
        assertThatThrownBy(() -> Order.of(orderTable, COOKING, LocalDateTime.now(), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태를_변경한다() {
        // given
        final Order order = TestFixtures.주문_생성(null, MEAL, LocalDateTime.now(),
                Collections.emptyList());

        // when
        order.updateOrderStatus(COMPLETION);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(COMPLETION);
    }

    @Test
    void 주문_상태_변경시_주문이_완료된_상태라면_예외가_발생한다() {
        // given
        final Order order = TestFixtures.주문_생성(null, COMPLETION, LocalDateTime.now(),
                Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> order.updateOrderStatus(COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
