package kitchenpos.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.exception.OrderLineItemEmptyException;
import kitchenpos.order.exception.OrderStatusAlreadyCompletionException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.exception.OrderTableEmptyException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Test
    void 주문_테이블이_비어있다면_예외를_발생한다() {
        // given
        OrderTable orderTable = 주문_테이블_생성(null, 10, true);

        // when & then
        assertThatThrownBy(() -> new Order(null, orderTable, COOKING.name(), LocalDateTime.now(), List.of()))
                .isInstanceOf(OrderTableEmptyException.class);
    }

    @Test
    void 주문_품목이_비어있다면_예외를_발생시킨다() {
        // given
        OrderTable orderTable = 주문_테이블_생성(null, 10, false);
        Order order = 주문_생성(orderTable, COOKING.name(), LocalDateTime.now(), List.of());

        // when & then
        assertThatThrownBy(() -> order.initOrderItems(List.of()))
                .isInstanceOf(OrderLineItemEmptyException.class);
    }

    @Test
    void 이미_완료된_주문은_바꿀_수_없다() {
        // given
        OrderTable orderTable = 주문_테이블_생성(null, 10, false);
        Order order = 주문_생성(orderTable, COMPLETION.name(), LocalDateTime.now(), List.of());

        // when & then
        assertThatThrownBy(() -> order.changeOrderStatus(MEAL.name()))
                .isInstanceOf(OrderStatusAlreadyCompletionException.class);
    }
}
