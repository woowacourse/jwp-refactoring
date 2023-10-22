package kitchenpos.domain.table;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @Test
    void 주문_테이블_손님_수가_0보다_작으면_예외가_발생한다() {
        // given
        int invalidNumberOfGuests = -1;

        // expect
        assertThatThrownBy(() -> new OrderTable(invalidNumberOfGuests, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블의 손님 수가 0보다 커야합니다");
    }

    @Test
    void 테이블_인원을_변경할_때_빈_테이블이면_예외가_발생한다() {
        // given
        OrderTable orderTable = new OrderTable(10, true);

        // when
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("인원을 변경할 테이블은 빈 테이블일 수 없습니다");
    }

    @Test
    void 단체_지정을_해제한다() {
        // given
        OrderTable orderTable = new OrderTable(1L, 10, false);

        // when
        orderTable.ungroup();

        // then
        assertThat(orderTable.getTableGroupId()).isNull();
    }
}
