package kitchenpos.domain;

import static java.util.List.of;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.fixture.OrderLineItemFixture.주문_항목;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTest {

    @Test
    void 주문의_상태를_변경할_때_이미_완료된_주문이라면_예외를_던진다() {
        // given
        OrderLineItem orderLineItem = 주문_항목(1L, 2);
        Order order = new Order(new OrderTable(0, false), of(orderLineItem));
        order.changeOrderStatus(COMPLETION);

        // expect
        assertThatThrownBy(() -> order.changeOrderStatus(COOKING))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료된 주문의 상태는 변경할 수 없습니다.");
    }

    @Test
    void 빈_테이블인_경우_예외를_던진다() {
        // given
        OrderTable orderTable = new OrderTable(0, true);

        // expect
        assertThatThrownBy(() -> new Order(orderTable, of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블인 경우 주문을 할 수 없습니다.");
    }
}
