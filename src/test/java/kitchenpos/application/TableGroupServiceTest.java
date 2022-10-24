package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest{

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("테이블 그룹의 사이즈가 1이면 예외가 발생한다.")
    void createWithInvalidSIze() {
        // given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(getOrderTable(1L)));

        // when
        final OrderTable savedOrderTable = getOrderTable(1L);
        savedOrderTable.setId(1L);
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(savedOrderTable));

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있지 않은 주문 테이블로 테이블 그룹을 생성하면 예외가 발생한다.")
    void createWithInvalidOrderTable() {
        // given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(getOrderTables());

        final OrderTable savedOrderTable1 = new OrderTable();
        final OrderTable savedOrderTable2 = new OrderTable();
        savedOrderTable1.setId(1L);
        savedOrderTable1.setEmpty(true);
        savedOrderTable2.setId(2L);

        // when
        savedOrderTable2.setEmpty(false);
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(savedOrderTable1, savedOrderTable2));

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 테이블 그룹이 있는 주문 테이블로 테이블 그룹을 생성하면 예외가 발생한다.")
    void createWithInvalidOrderTables() {
        // given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(getOrderTables());

        final OrderTable savedOrderTable1 = new OrderTable();
        final OrderTable savedOrderTable2 = new OrderTable();
        savedOrderTable1.setId(1L);
        savedOrderTable1.setEmpty(true);
        savedOrderTable2.setId(2L);
        savedOrderTable2.setEmpty(true);

        // when
        savedOrderTable2.setTableGroupId(2L);
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(savedOrderTable1, savedOrderTable2));

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다.")
    void unGroup() {
        // given
        final List<OrderTable> orderTables = getOrderTables();
        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);

        // when
        tableGroupService.ungroup(1L);

        // then
        assertAll(
                () -> assertThat(orderTables.get(0).getTableGroupId()).isNull(),
                () -> assertThat(orderTables.get(0).isEmpty()).isFalse(),
                () -> assertThat(orderTables.get(1).getTableGroupId()).isNull(),
                () -> assertThat(orderTables.get(1).isEmpty()).isFalse()
        );
    }

    private List<OrderTable> getOrderTables() {
        final OrderTable orderTable1 = new OrderTable();
        final OrderTable orderTable2 = new OrderTable();
        orderTable1.setId(1L);
        orderTable2.setId(2L);
        return Arrays.asList(orderTable1, orderTable2);
    }
}
