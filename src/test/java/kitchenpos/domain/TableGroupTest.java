package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableGroupTest {

    @Test
    void 테이블_그룹에_테이블을_추가한다() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.addOrderTables(List.of(new OrderTable(3, false), new OrderTable(2, false)));

        assertThat(tableGroup.getOrderTables()).hasSize(2);
    }

    @Test
    void 테이블_그룹에_두_개_미만의_테이블_추가_시_예외발생() {
        TableGroup tableGroup = new TableGroup();

        assertThatThrownBy(() -> tableGroup.addOrderTables(List.of(new OrderTable(3, false))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹에_추가된_테이블은_주문할_수_있는_상태다() {
        TableGroup tableGroup = new TableGroup();

        OrderTable orderTable1 = new OrderTable(3, false);
        OrderTable orderTable2 = new OrderTable(2, false);
        tableGroup.addOrderTables(List.of(orderTable1, orderTable2));

        assertAll(
                () -> assertThat(orderTable1.isEmpty()).isFalse(),
                () -> assertThat(orderTable2.isEmpty()).isFalse(),
                () -> assertThat(orderTable1.existsTableGroup()).isTrue(),
                () -> assertThat(orderTable2.existsTableGroup()).isTrue()
        );
    }
}
