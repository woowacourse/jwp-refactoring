package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.order.domain.MenuProductSnapShot;
import kitchenpos.order.domain.MenuSnapShot;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("주문 테이블(OrderTable) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderTableTest {

    private final MenuSnapShot menuSnapshot = new MenuSnapShot(
            "메뉴그룹",
            "말랑",
            BigDecimal.valueOf(20),
            List.of(
                    new MenuProductSnapShot("상품", BigDecimal.valueOf(10), 2)
            )
    );

    @Nested
    class 비어있음_상태_변경_검증_시 {

        @Test
        void 그룹에_속한_테이블의_경우_예외() {
            // given
            OrderTable orderTable = new OrderTable(10, true);
            orderTable.grouping(mock(TableGroup.class));

            // when & then
            assertThatThrownBy(() ->
                    orderTable.setEmpty(true)
            ).isInstanceOf(OrderTableException.class)
                    .hasMessage("그룹에 속한 테이블은 비어있음 상태를 변경할 수 없습니다.");
        }

        @Test
        void 조리_혹은_식사중_상태의_테이블이라면_예외() {
            // given
            OrderTable orderTable = new OrderTable(10, false);
            ReflectionTestUtils.setField(orderTable, "id", 2L);
            new Order(orderTable, List.of(new OrderLineItem(menuSnapshot, 10)));

            // when & then
            assertThatThrownBy(() ->
                    orderTable.setEmpty(false)
            ).isInstanceOf(OrderTableException.class)
                    .hasMessage("조리 혹은 식사중 상태의 테이블의 비어있음 상태는 변경할 수 없습니다.");
        }

        @Test
        void 그룹에_속하지_않은_테이블_중_계산_완료되었거나_주문하지_않은_테이블의_상태는_변경_가능() {
            // given
            OrderTable completeOrderTable = new OrderTable(10, false);
            ReflectionTestUtils.setField(completeOrderTable, "id", 2L);
            new Order(completeOrderTable, List.of(new OrderLineItem(menuSnapshot, 10)))
                    .setOrderStatus(OrderStatus.COMPLETION.name());

            OrderTable notInGroupTable = new OrderTable(10, false);
            ReflectionTestUtils.setField(notInGroupTable, "id", 3L);

            // when & then
            assertDoesNotThrow(() ->
                    completeOrderTable.setEmpty(true)
            );
            assertDoesNotThrow(() ->
                    notInGroupTable.setEmpty(true)
            );
        }
    }

    @Nested
    class 그룹화_시 {

        @Test
        void 비어있지_않은_상태가_된다() {
            // given
            OrderTable orderTable = new OrderTable(1, true);

            // when
            orderTable.grouping(mock(TableGroup.class));

            // then
            assertThat(orderTable.isEmpty()).isFalse();
        }
    }

    @Nested
    class 언그룹_시 {

        @Test
        void 테이블_그룹이_사라진다() {
            // given
            OrderTable orderTable = new OrderTable(1, true);
            orderTable.grouping(mock(TableGroup.class));

            // when
            orderTable.ungroup();

            // then
            assertThat(orderTable.getTableGroup()).isNull();
        }

        @Test
        void 조리_혹은_식사중_상태의_테이블이라면_예외() {
            // given
            OrderTable orderTable = new OrderTable(10, false);
            ReflectionTestUtils.setField(orderTable, "id", 2L);
            new Order(orderTable, List.of(new OrderLineItem(menuSnapshot, 10)));

            // when & then
            assertThatThrownBy(() ->
                    orderTable.ungroup()
            ).isInstanceOf(OrderTableException.class)
                    .hasMessage("조리 혹은 식사중 상태의 테이블이 포함되어 있어 그룹을 해제할 수 없습니다.");
        }
    }

    @Nested
    class 손님_수_변경_시 {

        @Test
        void 변경할_손님_수가_0명_미만이라면_예외() {
            // given
            OrderTable orderTable = new OrderTable(10, false);

            // when & then
            assertThatThrownBy(() ->
                    orderTable.setNumberOfGuests(-1)
            ).isInstanceOf(OrderTableException.class)
                    .hasMessage("손님 수는 0명 이상이어야 합니다.");
        }

        @Test
        void 테이블이_비어있으면_예외() {
            // given
            OrderTable orderTable = new OrderTable(10, true);

            // when & then
            assertThatThrownBy(() ->
                    orderTable.setNumberOfGuests(11)
            ).isInstanceOf(OrderTableException.class)
                    .hasMessage("비어있지 않은 테이블의 경우 손님 수를 변경할 수 없습니다.");
        }

        @Test
        void 테이블이_비어있지_않으며_변경할_손님_수가_0_이상이라면_변경_가능() {
            // given
            OrderTable orderTable = new OrderTable(10, false);

            // when & then
            assertDoesNotThrow(() ->
                    orderTable.setNumberOfGuests(11)
            );
        }
    }
}
