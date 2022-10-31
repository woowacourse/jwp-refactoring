package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class OrderTableTest {

    @Test
    @DisplayName("단체 지정한다")
    void setTableGroup() {
        // given
        OrderTable orderTable = new OrderTable(99, false);
        OrderTable orderTable2 = new OrderTable(99, false);

        // when
        TableGroup tableGroup = new TableGroup(List.of(orderTable, orderTable2));

        // then
        assertThat(orderTable.getTableGroup()).isEqualTo(tableGroup);
    }

    @Test
    @DisplayName("빈 테이블을 단체 지정할 수 없다")
    void setTableGroupForEmptyTable() {
        // given
        OrderTable orderTable = new OrderTable(99, true);
        OrderTable orderTable2 = new OrderTable(99, true);

        // when, then
        assertThatThrownBy(() -> new TableGroup(List.of(orderTable, orderTable2)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("지정할 단체가 null일 수 없다")
    void setNullTableGroup() {
        // given
        OrderTable orderTable = new OrderTable(99, false);

        // when, then
        assertThatThrownBy(() -> orderTable.setTableGroup(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("고객 수를 변경한다")
    void setNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable(99, false);

        // when
        orderTable.setNumberOfGuests(100);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(100);
    }

    @Test
    @DisplayName("빈 테이블의 고객 수를 변경할 수 없다")
    void setNumberOfGuestsForEmptyTable() {
        // given
        OrderTable orderTable = new OrderTable(99, true);

        // when, then
        assertThatThrownBy(() -> orderTable.setNumberOfGuests(100))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("변경할 고객 수가 음수일 수 없다")
    void minusNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable(99, false);

        // when, then
        assertThatThrownBy(() -> orderTable.setNumberOfGuests(-100))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("점유 여부를 변경한다")
    void setEmpty() {
        // given
        OrderTable orderTable = new OrderTable(99, false);

        // when
        orderTable.setEmpty(true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("단체 지정된 테이블의 점유 여부를 변경할 수 없다")
    void setEmptyForGroupedTable() {
        // given
        OrderTable orderTable = new OrderTable(99, false);
        OrderTable orderTable2 = new OrderTable(99, false);
        TableGroup tableGroup = new TableGroup(List.of(orderTable, orderTable2));

        // when, then
        assertThatThrownBy(() -> orderTable.setEmpty(true))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
