package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTest {

    @Test
    void 완료된_주문의_상태를_변경하려는_경우_예외가_발생한다() {
        // given
        final Order order = new Order(null, OrderStatus.COMPLETION, null);

        // expect
        assertThatThrownBy(() -> order.updateStatus("COOKING"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 완료된 주문입니다");
    }

    @ParameterizedTest(name = "완료된 주문이 아니라면 예외를 발생시킬 수 있다 name = {0}")
    @ValueSource(strings = {"COOKING", "MEAL"})
    void 완료된_주문이_아니라면_예외를_발생시킬_수_있다(final String status) {
        // given
        final Order order = new Order(null, OrderStatus.valueOf(status), null);

        // expect
        assertThatThrownBy(order::validateUncompleted)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("완료되지 않은 주문입니다");
    }

    @Test
    void 주문_테이블_아이디를_받아_주문을_생성할_수_있다() {
        // given
        final Long orderTableId = 1L;

        // when
        final Order order = Order.createBy(orderTableId);

        // then
        assertAll(
                () -> assertThat(order.getOrderedTime()).isNotNull(),
                () -> assertThat(order.getOrderStatus()).isEqualTo("COOKING"),
                () -> assertThat(order.getOrderTableId()).usingRecursiveComparison().isEqualTo(1L)
        );
    }

    @Test
    void 주문_테이블이_비어있을_경우_예외가_발생한다() {
        // given
        final OrderTable orderTable = new OrderTable(null, 3, true);

        // expect
        assertThatThrownBy(() -> Order.createBy(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 생성시 주문 테이블은 비어있을 수 없습니다");
    }


}
