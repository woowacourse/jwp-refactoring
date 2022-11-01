package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupCreateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends ServiceTest {
    @Test
    @DisplayName("테이블 그룹을 생성한다")
    void create() {
        final List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(saveAndGetOrderTable(1L, true));
        orderTables.add(saveAndGetOrderTable(2L, true));

        final TableGroupCreateResponse actual =
                tableGroupService.create(new TableGroupCreateRequest(orderTables));

        assertThat(actual.getOrderTables()).hasSize(2);
    }

    @Test
    @DisplayName("테이블 개수가 1개 이하일 때 그룹을 생성하면 예외를 반환한다")
    void create_oneTableException() {
        final List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(saveAndGetOrderTable(1L, true));

        assertThatThrownBy(() -> tableGroupService.create(new TableGroupCreateRequest(orderTables)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있지 않은 테이블로 그룹을 생성하면 예외를 반환한다")
    void create_notEmptyTableException() {
        final List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(saveAndGetOrderTable(1L, true));
        orderTables.add(saveAndGetOrderTable(2L, false));

        assertThatThrownBy(() -> tableGroupService.create(new TableGroupCreateRequest(orderTables)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다")
    void ungroup() {
        final List<OrderTable> orderTables = new ArrayList<>();
        final OrderTable orderTable1 = saveAndGetOrderTable(1L, true);
        final OrderTable orderTable2 = saveAndGetOrderTable(2L, true);
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);

        final TableGroupCreateResponse tableGroup =
                tableGroupService.create(new TableGroupCreateRequest(orderTables));

        tableGroupService.ungroup(tableGroup.getId());

        final OrderTable actual1 = orderTableDao.findById(1L)
                .orElseThrow(IllegalArgumentException::new);
        final OrderTable actual2 = orderTableDao.findById(2L)
                .orElseThrow(IllegalArgumentException::new);

        assertAll(
                () -> assertThat(actual1.getTableGroupId()).isNull(),
                () -> assertThat(actual2.getTableGroupId()).isNull()
        );
    }

    @Test
    @DisplayName("`계산 완료` 상태가 아닌 테이블이 포함된 테이블 그룹을 해제하면 예외를 반환한다")
    void ungroup_cookingException() {
        final List<OrderTable> orderTables = new ArrayList<>();
        final OrderTable orderTable1 = saveAndGetOrderTable(1L, true);
        final OrderTable orderTable2 = saveAndGetOrderTable(1L, true);
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);

        final TableGroupCreateResponse tableGroup =
                tableGroupService.create(new TableGroupCreateRequest(orderTables));
        final List<OrderTable> orderTablesInGroup = tableGroup.getOrderTables();

        saveAndGetOrderInOrderTable(1L, orderTablesInGroup.get(0), OrderStatus.COOKING.name());

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
