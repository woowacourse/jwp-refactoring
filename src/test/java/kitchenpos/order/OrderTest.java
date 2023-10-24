package kitchenpos.order;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.order.domain.MenuProductSnapShot;
import kitchenpos.order.domain.MenuSnapShot;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderException;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("주문(Order) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderTest {

    private final OrderTableRepository orderTableRepository = mock(OrderTableRepository.class);
    private final OrderValidator orderValidator = new OrderValidator(orderTableRepository);
    private final MenuSnapShot menuSnapshot = new MenuSnapShot(
            "메뉴그룹",
            "말랑",
            BigDecimal.valueOf(20),
            List.of(
                    new MenuProductSnapShot("상품", BigDecimal.valueOf(10), 2)
            )
    );

    @Nested
    class 주문_생성_시 {

        @Test
        void 주문_목록이_비어있는_경우_예외() {
            // given
            given(orderTableRepository.getById(1L))
                    .willReturn(new OrderTable(1, true));
            Order order = new Order(
                    1L,
                    List.of()
            );

            // when & then
            assertThatThrownBy(() -> order.place(orderValidator))
                    .isInstanceOf(OrderException.class)
                    .hasMessage("주문 목록이 비어있는 경우 주문하실 수 없습니다.");
        }

        @Test
        void 주문을_한_테이블이_비어있으면_예외() {
            // given
            given(orderTableRepository.getById(1L))
                    .willReturn(new OrderTable(1, true));
            Order order = new Order(
                    1L,
                    List.of(
                            new OrderLineItem(menuSnapshot, 10)
                    )
            );

            // when & then
            assertThatThrownBy(() -> order.place(orderValidator))
                    .isInstanceOf(OrderException.class)
                    .hasMessage("비어있는 테이블에서는 주문할 수 없습니다.");
        }

        @Test
        void 주문_목록이_비어있지_않고_주문을_한_테이블이_비어있지_않은_경우_주문할_수_있다() {
            // given
            given(orderTableRepository.getById(1L))
                    .willReturn(new OrderTable(1, false));
            Order order = new Order(
                    1L,
                    List.of(
                            new OrderLineItem(menuSnapshot, 10)
                    )
            );

            // when & then
            assertDoesNotThrow(() -> order.place(orderValidator));
        }
    }

    @Nested
    class 상태_변경_시 {

        @Test
        void 결제되지_않은_주문의_상태를_변경한다() {
            // given
            given(orderTableRepository.getById(1L))
                    .willReturn(new OrderTable(1, false));
            Order order = new Order(
                    1L,
                    List.of(
                            new OrderLineItem(menuSnapshot, 10)
                    ));

            // when
            order.setOrderStatus(COMPLETION.name());

            // then
            assertThat(order.getOrderStatus()).isEqualTo(COMPLETION.name());
        }

        @Test
        void 이미_결제_완료된_주문은_상태를_변경할_수_없다() {
            // given
            given(orderTableRepository.getById(1L))
                    .willReturn(new OrderTable(1, false));

            Order order = new Order(
                    1L,
                    List.of(
                            new OrderLineItem(menuSnapshot, 10)
                    ));
            order.setOrderStatus(COMPLETION.name());

            // when & then
            assertThatThrownBy(() ->
                    order.setOrderStatus(OrderStatus.MEAL.name())
            ).isInstanceOf(OrderException.class)
                    .hasMessage("이미 결제 완료된 주문은 상태를 변경할 수 없습니다.");
        }
    }
}
