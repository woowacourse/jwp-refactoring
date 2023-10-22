package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import kitchenpos.fixture.OrderFixture;
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
        Order order = OrderFixture.builder().build();
        assertThatThrownBy(() -> {
            orderTable.order(order);
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 테이블은 비어있습니다.");
    }

    @Test
    void 그룹화된_주문_테이블의_빈_여부를_바꾸면_예외() {
        // given
        OrderTable orderTable = new OrderTable(1L, new TableGroup(1L, LocalDateTime.now()), 100, true);

        // when && then
        assertThatThrownBy(() -> orderTable.changeEmpty(false))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 주문 테이블은 그룹화 되어있기 때문에 빈 여부를 변경할 수 없습니다");
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
