package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderLineItemFixture.주문_항목;
import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.vo.MenuSpecification;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderTest {

    @Nested
    class 주문을_생성할_때 {

        @Test
        void 정상적으로_생성한다() {
            // given
            Long orderTableId = 1L;
            MenuSpecification menuSpecification = new MenuSpecification("menuName", BigDecimal.ONE);
            List<OrderLineItem> orderLineItems = List.of(주문_항목(1L, menuSpecification));
            long menuCount = 1L;

            // expect
            assertThatNoException().isThrownBy(() -> Order.of(orderTableId, orderLineItems, menuCount));
        }

        @Test
        void 주문_항목_목록이_없으면_예외를_던진다() {
            // given
            Long orderTableId = 1L;
            List<OrderLineItem> invalidOrderLineItems = List.of();
            long menuCount = 0L;

            // expect
            assertThatThrownBy(() -> Order.of(orderTableId, invalidOrderLineItems, menuCount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 항목이 입력되지 않았습니다.");
        }

        @Test
        void 주문_항목_목록_크기와_메뉴_수가_다르면_예외를_던진다() {
            // given
            Long orderTableId = 1L;
            MenuSpecification menuSpecification = new MenuSpecification("menuName", BigDecimal.ONE);
            List<OrderLineItem> orderLineItems = List.of(주문_항목(1L, menuSpecification));
            long invalidMenuCount = orderLineItems.size() + 1;

            // expect
            assertThatThrownBy(() -> Order.of(orderTableId, orderLineItems, invalidMenuCount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴에 없는 항목을 주문할 수 없습니다.");
        }
    }

    @Nested
    class 주문_상태를_변경할_때 {

        @Test
        void 계산_완료된_주문이라면_예외를_던진다() {
            // given
            Order order = new Order(1L, COMPLETION, LocalDateTime.now(),
                    List.of(주문_항목(1L, new MenuSpecification("menuName", BigDecimal.ONE))));

            // expect
            assertThatThrownBy(() -> order.changeOrderStatus(COOKING.name()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("계산 완료된 주문은 주문 상태를 변경할 수 없습니다.");
        }

        @Test
        void 정상적으로_변경한다() {
            // given
            Order order = new Order(1L, COOKING, LocalDateTime.now(),
                    List.of(주문_항목(1L, new MenuSpecification("menuName", BigDecimal.ONE))));

            // when
            order.changeOrderStatus(MEAL.name());

            // then
            assertThat(order.getOrderStatus()).isEqualTo(MEAL);
        }
    }
}
