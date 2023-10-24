package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTest {

    @Test
    void 주문_테이블이_비어있는_경우_예외가_발생한다() {
        // given
        final var orderTables = OrderTableFixture.빈테이블_1명();
        final var createdDate = LocalDateTime.now();
        final var orderLineItems = Collections.singletonList(OrderLineItemFixture.주문항목_망고치킨_2개());

        // when & then
        assertThatThrownBy(() -> new Order(orderTables, COOKING, createdDate, orderLineItems))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목이_비어있는_경우_예외가_발생한다() {
        // given
        final var orderTables = OrderTableFixture.주문테이블_N명(2);
        final var createdDate = LocalDateTime.now();
        final var orderLineItems = Collections.<OrderLineItem>emptyList();

        // when & then
        assertThatThrownBy(() -> new Order(orderTables, COOKING, createdDate, orderLineItems))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Nested
    class 주문_상태_변경 {

        @Test
        void 주문_상태를_식사로_변경할_수_있다() {
            // given
            final var orderTables = OrderTableFixture.주문테이블_N명(2);
            final var createdDate = LocalDateTime.now();
            final var orderLineItems = List.of(OrderLineItemFixture.주문항목_망고치킨_2개());
            final var order = new Order(orderTables, COOKING, createdDate, orderLineItems);

            // when
            order.changeOrderStatus(MEAL);

            // then
            assertThat(order.getOrderStatus()).isEqualTo(MEAL);
        }

        @Test
        void 주문_상태를_계산완료로_변경할_수_있다() {
            // given
            final var orderTables = OrderTableFixture.주문테이블_N명(2);
            final var createdDate = LocalDateTime.now();
            final var orderLineItems = List.of(OrderLineItemFixture.주문항목_망고치킨_2개());
            final var order = new Order(orderTables, COOKING, createdDate, orderLineItems);

            // when
            order.changeOrderStatus(COMPLETION);

            // then
            assertThat(order.getOrderStatus()).isEqualTo(COMPLETION);
        }

        @Test
        void 주문_상태가_이미_계산완료면_예외가_발생한다() {
            // given
            final var orderTables = OrderTableFixture.주문테이블_N명(2);
            final var createdDate = LocalDateTime.now();
            final var orderLineItems = List.of(OrderLineItemFixture.주문항목_망고치킨_2개());
            final var order = new Order(orderTables, COMPLETION, createdDate, orderLineItems);

            // when & then
            assertThatThrownBy(() -> order.changeOrderStatus(COMPLETION))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
