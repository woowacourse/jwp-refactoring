package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
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

    @Nested
    class 주문_상태_변경 {

        @Test
        void 주문_상태를_식사로_변경할_수_있다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_N명(2);
            final var createdDate = LocalDateTime.now();
            final var orderLineItems = List.of(OrderLineItemFixture.주문항목_망고치킨_2개());
            final var order = new Order(orderTable.getId(), COOKING, createdDate, orderLineItems);

            // when
            order.changeOrderStatus(MEAL);

            // then
            assertThat(order.getOrderStatus()).isEqualTo(MEAL);
        }

        @Test
        void 주문_상태를_계산완료로_변경할_수_있다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_N명(2);
            final var createdDate = LocalDateTime.now();
            final var orderLineItems = List.of(OrderLineItemFixture.주문항목_망고치킨_2개());
            final var order = new Order(orderTable.getId(), COOKING, createdDate, orderLineItems);

            // when
            order.changeOrderStatus(COMPLETION);

            // then
            assertThat(order.getOrderStatus()).isEqualTo(COMPLETION);
        }
    }
}
