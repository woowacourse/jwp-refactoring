package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class OrderTest {

    @Nested
    class Constructor_성공_테스트 {
    }

    @Nested
    class Constructor_실패_테스트 {

        @Test
        void 주문_테이블이_비어있는_경우_예러를_반환한다() {
            // given
            final var orderTable = new OrderTable(new TableGroup(), 3, true);

            // when & then
            assertThatThrownBy(() -> new Order(orderTable, OrderStatus.COOKING, List.of()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 주문 테이블이 비어있습니다.");
        }

        @Test
        void 주문_항목이_비어있는_경우_예러를_반환한다() {
            // given
            final var orderTable = new OrderTable(new TableGroup(), 3, false);

            // when & then
            assertThatThrownBy(() -> new Order(orderTable, OrderStatus.COOKING, List.of()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 주문 항목이 비어있습니다.");
        }
    }

    @Nested
    class changeOrderStatus_성공_테스트 {

        @Test
        void 주문_상태를_변경한다() {
            // given
            final var orderTable = new OrderTable();
            final var order = new Order(orderTable, OrderStatus.COOKING, List.of(new OrderLineItem()));

            // when
            order.changeOrderStatus(OrderStatus.MEAL);

            // then
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
        }
    }

    @Nested
    class changeOrderStatus_실패_테스트 {

        @Test
        void 완료된_주문은_상태를_변경할_수_없다() {
            // given
            final var orderTable = new OrderTable();
            final var order = new Order(orderTable, OrderStatus.COMPLETION, List.of(new OrderLineItem()));

            // when & then
            assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 완료된 주문은 상태 변경이 불가능합니다.");
        }
    }

    @Nested
    class applyOrderLineItem_성공_테스트 {

        @Test
        void 주문_항목을_변경할_수_있다() {
            // given
            final var orderTable = new OrderTable();
            final var order = new Order(orderTable, OrderStatus.COMPLETION, List.of(new OrderLineItem()));
            final var newOrderLineItem = new OrderLineItem();

            // when
            order.applyOrderLineItem(List.of(newOrderLineItem));

            // then
            assertThat(order.getOrderLineItems().get(0)).isEqualTo(newOrderLineItem);
        }
    }

    @Nested
    class applyOrderLineItem_실패_테스트 {
    }

    @Nested
    class isNotCompletion_성공_테스트 {

        @Test
        void 주문이_아직_완료되지_않으면_True를_반환한다() {
            // given
            final var orderTable = new OrderTable();
            final var order = new Order(orderTable, OrderStatus.MEAL, List.of(new OrderLineItem()));

            // when
            final boolean actual = order.isNotCompletion();

            // then
            assertThat(actual).isTrue();
        }

        @Test
        void 주문이_완료되었으면_False를_반환한다() {
            // given
            final var orderTable = new OrderTable();
            final var order = new Order(orderTable, OrderStatus.COMPLETION, List.of(new OrderLineItem()));

            // when
            final boolean actual = order.isNotCompletion();

            // then
            assertThat(actual).isFalse();
        }
    }

    @Nested
    class isNotCompletion_실패_테스트 {
    }
}
