package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTest {

    @Test
    void 상태_변경() {
        // given
        Order order = new Order(null, OrderStatus.COOKING, LocalDateTime.now(), new ArrayList<>());
        OrderStatus changedOrderStatus = OrderStatus.COMPLETION;

        // when
        order.changeOrderStatus(changedOrderStatus);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(changedOrderStatus);
    }

    @Test
    void 완료된_주문_상태는_변경_불가() {
        // given
        Order order = new Order(null, OrderStatus.COMPLETION, LocalDateTime.now(), new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
