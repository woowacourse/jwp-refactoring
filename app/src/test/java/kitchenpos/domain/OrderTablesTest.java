package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;

class OrderTablesTest {

    @DisplayName("OrderTables가 가진 테이블 개수와 입력받은 값이 일치하는지 확인한다.")
    @Test
    void checkTableSizeIsEqual() {
        // given
        final OrderTables orderTables = new OrderTables(List.of(new OrderTable(), new OrderTable()));

        // when & then
        assertDoesNotThrow(() -> orderTables.checkTableSizeIsEqual(2));
    }

    @DisplayName("OrderTables가 가진 테이블 개수와 입력받은 값이 일치하지 않으면 예외 처리한다.")
    @Test
    void checkTableSizeIsEqual_FailWhenTableSizeNotMatch() {
        // given

        final OrderTables orderTables = new OrderTables(List.of(new OrderTable(), new OrderTable()));

        // when & then
        assertThatThrownBy(() -> orderTables.checkTableSizeIsEqual(3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 수가 일치하지 않습니다.");
    }
}
