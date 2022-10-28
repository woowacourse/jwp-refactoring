package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import kitchenpos.fixture.OrderTableFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @DisplayName("테이블 목록은 비어있을 수 없다")
    @Test
    void orderTablesIsEmpty_throwsException() {
        final List<OrderTable> emptyOrderTables = Collections.emptyList();

        assertThatThrownBy(
                () -> new TableGroup(emptyOrderTables)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 목록은 크기가 1이 될 수 없다")
    @Test
    void orderTablesSizeIsOne_throwsException() {
        final var table = OrderTableFactory.emptyTable(1);
        final var oneInOrderTables = List.of(table);

        assertThatThrownBy(
                () -> new TableGroup(oneInOrderTables)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
