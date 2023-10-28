package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTableTest {

    @Test
    void 비어있는_주문_테이블에_주문을_요청하면_예외() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 100, true);

        // when && then
        assertThatThrownBy(() -> {
            orderTable.order(List.of(OrderLineItemFixture.builder().build()));
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 테이블은 비어있습니다.");
    }

    @Test
    void 빈_테에블의_손님_수를_바꾸면_예외() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 100, true);

        // when && then
        assertThatThrownBy(() -> orderTable.changeNumbersOfGuests(10))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("빈 테이블은 손님 수를 바꿀 수 없습니다");
    }

    @Test
    void 손님_수를_0_이하로_바꿀_수는_없다() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 100, false);

        // when && then
        assertThatThrownBy(() -> orderTable.changeNumbersOfGuests(-1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("손님 수는 0보다 커야합니다");
    }
}
