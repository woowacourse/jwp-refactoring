package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.exception.OrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("주문 검증기(OrderValidator) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderValidatorTest {

    private final MenuDao menuDao = mock(MenuDao.class);
    private final OrderTableDao orderTableDao = mock(OrderTableDao.class);
    private final OrderValidator orderValidator = new OrderValidator(menuDao, orderTableDao);

    @Nested
    class 주문_생성_시 {

        @Test
        void 주문_목록이_비어있는_경우_예외() {
            // when & then
            assertThatThrownBy(() ->
                    orderValidator.validateCreate(List.of(), 1L)
            ).isInstanceOf(OrderException.class)
                    .hasMessage("주문 목록이 비어있는 경우 주문하실 수 없습니다.");
        }

        @Test
        void 주문_목록의_상품_ID들이_실제_존재하는_메뉴_ID와_일치하지_않는다면_오류() {
            // given
            given(menuDao.countByIdIn(List.of(1L)))
                    .willReturn(0L);

            // when & then
            assertThatThrownBy(() ->
                    orderValidator.validateCreate(List.of(
                            new OrderLineItem(1L, 10)
                    ), 1L)
            ).isInstanceOf(OrderException.class)
                    .hasMessage("주문 메뉴중 존재하지 않는 메뉴가 있습니다.");
        }

        @Test
        void 주문을_한_테이블이_비어있으면_예외() {
            // given
            given(menuDao.countByIdIn(List.of(1L)))
                    .willReturn(1L);
            given(orderTableDao.findById(1L))
                    .willReturn(Optional.of(new OrderTable(10, true)));

            // when & then
            assertThatThrownBy(() ->
                    orderValidator.validateCreate(List.of(
                            new OrderLineItem(1L, 10)
                    ), 1L)
            ).isInstanceOf(OrderException.class)
                    .hasMessage("비어있는 테이블에서는 주문할 수 없습니다.");
        }

        @Test
        void 주문_목록이_비어있지_않고_주문을_한_테이블이_비어있지_않은_경우_주문할_수_있다() {
            // given
            given(menuDao.countByIdIn(List.of(1L)))
                    .willReturn(1L);
            given(orderTableDao.findById(1L))
                    .willReturn(Optional.of(new OrderTable(10, false)));

            // when & then
            assertDoesNotThrow(() ->
                    orderValidator.validateCreate(List.of(
                            new OrderLineItem(1L, 10)
                    ), 1L)
            );
        }
    }
}
