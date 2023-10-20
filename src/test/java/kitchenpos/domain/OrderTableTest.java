package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static kitchenpos.fixture.OrderTableFixtrue.orderTable;
import static kitchenpos.fixture.TableGroupFixture.tableGroup;
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
    void 빈_테이블로_변경한다() {
        // given
        OrderTable orderTable = new OrderTable(10, false);

        // when
        orderTable.changeEmpty(true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
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
    void 빈_테이블로_변경할_때_단체_지정된_테이블을_변경하면_예외가_발생한다() {
        // given
        OrderTable orderTable1 = orderTable(1, false);
        OrderTable orderTable2 = orderTable(1, false);
        OrderTable orderTable = new OrderTable(tableGroup(List.of(orderTable1, orderTable2)), 10, false);

        // expect
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정된 테이블은 변경할 수 없습니다");

    }

    @Test
    void 단체_지정을_해제한다() {
        // given
        OrderTable orderTable1 = orderTable(1, false);
        OrderTable orderTable2 = orderTable(1, false);
        OrderTable orderTable = new OrderTable(tableGroup(List.of(orderTable1, orderTable2)), 10, false);

        // when
        orderTable.ungroup();

        // then
        assertThat(orderTable.getTableGroup()).isNull();
    }
}
