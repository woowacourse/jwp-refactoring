package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.exception.OrderTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 검증기(OrderTableValidator) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderValidatorTest {

    private final OrderDao orderDao = mock(OrderDao.class);
    private final OrderTableValidator orderTableValidator = new OrderTableValidator(orderDao);

    @Nested
    class 비어있음_상태_변경_검증_시 {

        @Test
        void 그룹에_속한_테이블의_경우_예외() {
            // given
            OrderTable orderTable = new OrderTable(10, false);
            orderTable.setTableGroupId(1L);

            // when & then
            assertThatThrownBy(() ->
                    orderTableValidator.validateChangeEmpty(orderTable)
            ).isInstanceOf(OrderTableException.class)
                    .hasMessage("그룹에 속한 테이블은 비어있음 상태를 변경할 수 없습니다.");
        }

        @Test
        void 조리_혹은_식사중_상태의_테이블이라면_예외() {
            // given
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(
                    2L,
                    Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
            )).willReturn(true);
            OrderTable orderTable = new OrderTable(10, false);
            orderTable.setId(2L);

            // when & then
            assertThatThrownBy(() ->
                    orderTableValidator.validateChangeEmpty(orderTable)
            ).isInstanceOf(OrderTableException.class)
                    .hasMessage("조리 혹은 식사중 상태의 테이블의 비어있음 상태는 변경할 수 없습니다.");
        }

        @Test
        void 그룹에_속하지_않았으면서_계산_완료된_테이블의_상태는_변경_가능() {
            // given
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(
                    2L,
                    Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
            )).willReturn(false);
            OrderTable orderTable = new OrderTable(10, false);
            orderTable.setId(2L);

            // when & then
            assertDoesNotThrow(() ->
                    orderTableValidator.validateChangeEmpty(orderTable)
            );
        }
    }
}
