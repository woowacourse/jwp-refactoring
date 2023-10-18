package kitchenpos.table;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableException;
import kitchenpos.table.domain.OrderTableValidator;
import kitchenpos.table.domain.TableGroup;
import org.hibernate.annotations.common.reflection.ReflectionUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("주문 테이블 검증기(OrderTableValidator) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderTableValidatorTest {

    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final OrderTableValidator orderTableValidator = new OrderTableValidator(orderRepository);

    @Nested
    class 비어있음_상태_변경_검증_시 {

        @Test
        void 그룹에_속한_테이블의_경우_예외() {
            // given
            OrderTable orderTable = new OrderTable(10, true);
            // TODO 변경
            orderTable.grouping(mock(TableGroup.class));

            // when & then
            assertThatThrownBy(() ->
                    orderTableValidator.validateChangeEmpty(orderTable)
            ).isInstanceOf(OrderTableException.class)
                    .hasMessage("그룹에 속한 테이블은 비어있음 상태를 변경할 수 없습니다.");
        }

        @Test
        void 조리_혹은_식사중_상태의_테이블이라면_예외() {
            // given
            given(orderRepository.existsByOrderTableIdAndOrderStatusIn(
                    2L,
                    Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
            )).willReturn(true);
            OrderTable orderTable = new OrderTable(10, false);
            ReflectionTestUtils.setField(orderTable, "id", 2L);

            // when & then
            assertThatThrownBy(() ->
                    orderTableValidator.validateChangeEmpty(orderTable)
            ).isInstanceOf(OrderTableException.class)
                    .hasMessage("조리 혹은 식사중 상태의 테이블의 비어있음 상태는 변경할 수 없습니다.");
        }

        @Test
        void 그룹에_속하지_않았으면서_계산_완료된_테이블의_상태는_변경_가능() {
            // given
            given(orderRepository.existsByOrderTableIdAndOrderStatusIn(
                    2L,
                    Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
            )).willReturn(false);
            OrderTable orderTable = new OrderTable(10, false);
            ReflectionTestUtils.setField(orderTable, "id", 2L);

            // when & then
            assertDoesNotThrow(() ->
                    orderTableValidator.validateChangeEmpty(orderTable)
            );
        }
    }
}
