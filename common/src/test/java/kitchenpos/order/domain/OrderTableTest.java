package kitchenpos.order.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.CannotChangeGroupedTableEmptyException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderTableTest {

    @Test
    void 단체_테이블에_속한_테이블을_빈_테이블로_변경_시_예외를_반환한다() {
        // given
        OrderTable orderTable = new OrderTable(4, false);
        orderTable.addToTableGroup(1L);

        // when, then
        assertThrows(CannotChangeGroupedTableEmptyException.class,
                () -> orderTable.changeEmpty(true));
    }
}
