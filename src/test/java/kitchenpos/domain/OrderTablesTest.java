package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderTablesTest {

    @Test
    void 주문_테이블_유효성을_검증한다() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        final OrderTables orderTables = new OrderTables(List.of(orderTable));

        // when, then
        assertThatCode(orderTables::validateOrderTables)
                .doesNotThrowAnyException();
    }

    @Test
    void 주문_테이블_유효성_검사시_주문_테이블이_비어있지_않은_경우_예외가_발생한다() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        final OrderTables orderTables = new OrderTables(List.of(orderTable));

        // when, then
        assertThatThrownBy(orderTables::validateOrderTables)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_유효성_검사시_주문_테이블이_그룹지정이_되어있는_경우_예외가_발생한다() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setTableGroupId(1L);
        final OrderTables orderTables = new OrderTables(List.of(orderTable));

        // when, then
        assertThatThrownBy(orderTables::validateOrderTables)
                .isInstanceOf(IllegalArgumentException.class);
    }
}
