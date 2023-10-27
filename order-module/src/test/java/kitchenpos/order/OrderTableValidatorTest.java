package kitchenpos.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableException;
import kitchenpos.domain.OrderTableValidator;
import kitchenpos.domain.OrderTableValidatorImpl;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("(OrderTableValidator) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class OrderTableValidatorTest {

    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final OrderTableValidator orderTableValidator = new OrderTableValidatorImpl(orderRepository);

    @Nested
    class 비어있음_상태_변경_검증_시 {

        @Test
        void 그룹에_속한_테이블의_경우_예외() {
            // given
            OrderTable orderTable = new OrderTable(10, true);
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
            OrderTable orderTable = new OrderTable(10, false);
            given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), anyList()))
                    .willReturn(true);

            // when & then
            assertThatThrownBy(() ->
                    orderTableValidator.validateChangeEmpty(orderTable)
            ).isInstanceOf(OrderTableException.class)
                    .hasMessage("조리 혹은 식사중 상태의 테이블의 비어있음 상태는 변경할 수 없습니다.");
        }

        @Test
        void 그룹에_속하지_않은_테이블_중_계산_완료되었거나_주문하지_않은_테이블의_상태는_변경_가능() {
            // given
            OrderTable completeOrderTable = new OrderTable(10, false);
            OrderTable notInGroupTable = new OrderTable(10, false);
            ReflectionTestUtils.setField(notInGroupTable, "id", 3L);

            // when & then
            assertDoesNotThrow(() ->
                    orderTableValidator.validateChangeEmpty(completeOrderTable)
            );
            assertDoesNotThrow(() ->
                    orderTableValidator.validateChangeEmpty(notInGroupTable)
            );
        }
    }

    @Nested
    class 언그룹_검증_시 {

        @Test
        void 조리_혹은_식사중_상태의_테이블이_아니라면_성공() {
            // given
            OrderTable orderTable = new OrderTable(1, true);
            orderTable.grouping(mock(TableGroup.class));

            // when & then
            assertDoesNotThrow(() -> {
                orderTableValidator.validateUngroup(orderTable, "");
            });
        }

        @Test
        void 조리_혹은_식사중_상태의_테이블이라면_예외() {
            // given
            OrderTable orderTable = new OrderTable(10, false);
            given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), anyList()))
                    .willReturn(true);

            // when & then
            assertThatThrownBy(() ->
                    orderTableValidator.validateUngroup(orderTable, "조리 혹은 식사중 상태의 테이블이 포함되어 있어 그룹을 해제할 수 없습니다.")
            ).isInstanceOf(OrderTableException.class)
                    .hasMessage("조리 혹은 식사중 상태의 테이블이 포함되어 있어 그룹을 해제할 수 없습니다.");
        }
    }
}
