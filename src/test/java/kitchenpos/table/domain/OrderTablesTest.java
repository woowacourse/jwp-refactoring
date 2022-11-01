package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @Test
    @DisplayName("비어있다")
    void isEmpty() {
        OrderTables orderTables = new OrderTables(Collections.emptyList());

        assertThat(orderTables.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("크기가 더 작다.")
    void isSmallerThan() {
        OrderTables orderTables = new OrderTables(List.of(new OrderTable(10, true)));

        assertThat(orderTables.isSmallerThan(2)).isTrue();
    }

    @Test
    @DisplayName("value 중 어떤 것이든 사용 중이다.")
    void anyUsing() {
        OrderTables orderTables = new OrderTables(List.of(new OrderTable(10, true),
                new OrderTable(10, false)));

        assertThat(orderTables.anyUsing()).isTrue();
    }
}
