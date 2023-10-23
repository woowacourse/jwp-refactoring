package kitchenpos.table.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
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
        TableGroup.of(LocalDateTime.now(), List.of(orderTable));

        // when, then
        assertThrows(CannotChangeGroupedTableEmptyException.class, () -> orderTable.changeEmpty(true));
    }
}
