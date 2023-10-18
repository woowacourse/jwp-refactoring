package kitchenpos.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderException;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("주문 검증기(OrderValidator) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderValidatorTest {

    private final MenuRepository menuRepository = mock(MenuRepository.class);
    private final OrderTableRepository orderTableRepository = mock(OrderTableRepository.class);
    private final OrderValidator orderValidator = new OrderValidator(menuRepository, orderTableRepository);

    @Nested
    class 주문_생성_시 {

        @Test
        void 주문_목록이_비어있는_경우_예외() {
            // when & then
            assertThatThrownBy(() ->
                    orderValidator.validateCreate(List.of(), new OrderTable(1, true))
            ).isInstanceOf(OrderException.class)
                    .hasMessage("주문 목록이 비어있는 경우 주문하실 수 없습니다.");
        }

        @Test
        void 주문_목록의_상품_ID들이_실제_존재하는_메뉴_ID와_일치하지_않는다면_오류() {
            // given
            given(menuRepository.countByIdIn(List.of(1L)))
                    .willReturn(0L);

            // when & then
            assertThatThrownBy(() ->
                    orderValidator.validateCreate(List.of(
                            new OrderLineItem(1L, 10)
                    ), new OrderTable(1, true))
            ).isInstanceOf(OrderException.class)
                    .hasMessage("주문 메뉴중 존재하지 않는 메뉴가 있습니다.");
        }

        @Test
        void 주문을_한_테이블이_비어있으면_예외() {
            // given
            given(menuRepository.countByIdIn(List.of(1L)))
                    .willReturn(1L);

            // when & then
            assertThatThrownBy(() ->
                    orderValidator.validateCreate(List.of(
                            new OrderLineItem(1L, 10)
                    ), new OrderTable(1, true))
            ).isInstanceOf(OrderException.class)
                    .hasMessage("비어있는 테이블에서는 주문할 수 없습니다.");
        }

        @Test
        void 주문_목록이_비어있지_않고_주문을_한_테이블이_비어있지_않은_경우_주문할_수_있다() {
            // given
            given(menuRepository.countByIdIn(List.of(1L)))
                    .willReturn(1L);

            // when & then
            assertDoesNotThrow(() ->
                    orderValidator.validateCreate(List.of(
                            new OrderLineItem(1L, 10)
                    ), new OrderTable(1, false))
            );
        }
    }
}
