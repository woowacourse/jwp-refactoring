package kitchenpos.domain.order;

import static kitchenpos.domain.order.OrderStatus.COMPLETION;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.fixture.OrderLineItemFixture.주문_항목;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTest {

    @Test
    void 주문의_상태를_변경할_때_이미_완료된_주문이라면_예외를_던진다() {
        // given
        OrderLineItem orderLineItem = 주문_항목(1L, 2);
        Order order = new Order(1L, List.of(orderLineItem));
        order.changeOrderStatus(COMPLETION);

        // expect
        assertThatThrownBy(() -> order.changeOrderStatus(COOKING))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료된 주문의 상태는 변경할 수 없습니다.");
    }

    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @ParameterizedTest(name = "테이블의 주문 상태가 {0}인 경우 예외를 던진다")
    void 주문이_발생한_테이블의_상태를_변경_할_수_없는_경우_예외를_던진다(OrderStatus orderStatus) {
        // given
        OrderLineItem orderLineItem = 주문_항목(1L, 2);
        Order order = new Order(1L, List.of(orderLineItem));
        order.changeOrderStatus(orderStatus);

        // expect
        assertThatThrownBy(order::validateChangeTableStatusAllowed)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 주문 상태가 조리중이거나 식사중인 경우 테이블의 상태를 변경할 수 없습니다.");
    }

    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @ParameterizedTest(name = "테이블의 주문 상태가 {0}인 경우 예외를 던진다")
    void 주문이_발생한_테이블의_그룹을_해제할_수_없는_경우_예외를_던진다(OrderStatus orderStatus) {
        // given
        OrderLineItem orderLineItem = 주문_항목(1L, 2);
        Order order = new Order(1L, List.of(orderLineItem));
        order.changeOrderStatus(orderStatus);

        // expect
        assertThatThrownBy(order::validateUngroupTableAllowed)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 주문 상태가 조리중이거나 식사중인 경우 단체 지정 해제를 할 수 없습니다.");
    }
}
