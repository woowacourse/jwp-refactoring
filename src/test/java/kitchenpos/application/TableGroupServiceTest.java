package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends ServiceTest {
    @Test
    @DisplayName("테이블 그룹을 생성한다")
    void create() {
        final List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(saveAndGetOrderTable(1L, true));
        orderTables.add(saveAndGetOrderTable(2L, true));

        final TableGroup actual = tableGroupService.create(orderTables);

        assertThat(actual.getOrderTables()).hasSize(2);
    }

    @Test
    @DisplayName("테이블 개수가 1개 이하일 때 그룹을 생성하면 예외를 반환한다")
    void create_oneTableException() {
        final List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(saveAndGetOrderTable(1L, true));

        assertThatThrownBy(() -> tableGroupService.create(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있지 않은 테이블로 그룹을 생성하면 예외를 반환한다")
    void create_notEmptyTableException() {
        final List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(saveAndGetOrderTable(1L, true));
        orderTables.add(saveAndGetOrderTable(2L, false));

        assertThatThrownBy(() -> tableGroupService.create(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다")
    void ungroup() {
        final List<OrderTable> orderTables = new ArrayList<>();
        final OrderTable orderTable1 = saveAndGetOrderTable(1L, true);
        final OrderTable orderTable2 = saveAndGetOrderTable(1L, true);
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);

        final TableGroup tableGroup = tableGroupService.create(orderTables);

        tableGroupService.ungroup(tableGroup.getId());

        assertAll(
                () -> assertThat(orderTable1.getTableGroupId()).isNull(),
                () -> assertThat(orderTable2.getTableGroupId()).isNull()
        );
    }
}
