package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableGroupService tableGroupService;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    private TableGroup tableGroup;
    private List<OrderTable> orderTables;
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
        tableGroup = new TableGroup();
        tableGroup.setId(1L);
        orderTable1 = new OrderTable();
        orderTable2 = new OrderTable();
        orderTable1.setId(1L);
        orderTable2.setId(2L);
        orderTable1.setEmpty(true);
        orderTable2.setEmpty(true);
        orderTables = Arrays.asList(orderTable1, orderTable2);
        tableGroup.setOrderTables(orderTables);
    }

    @Test
    void create() {
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
        given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);
        given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);
        given(orderTableDao.save(orderTables.get(0))).willReturn(orderTables.get(0));
        given(orderTableDao.save(orderTables.get(1))).willReturn(orderTables.get(1));

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        List<OrderTable> savedOrderTables = savedTableGroup.getOrderTables();

        assertThat(savedTableGroup.getId()).isEqualTo(tableGroup.getId());
        assertThat(savedTableGroup.getCreatedDate()).isNotNull();
        assertThat(savedOrderTables.size()).isEqualTo(orderTables.size());
        assertThat(savedOrderTables.get(0).isEmpty()).isFalse();
        assertThat(savedOrderTables.get(1).isEmpty()).isFalse();
    }

    @ParameterizedTest
    @EmptySource
    @DisplayName("그룹으로 묶을 테이블 수는 2 이상이어야 한다.")
    void createErrorWhenTableNumberIsZero(List<OrderTable> tables) {
        tableGroup.setOrderTables(tables);
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("create Error: 그룹으로 묶을 테이블은 2개 이상이어야 합니다.");
    }

    @Test
    @DisplayName("그룹으로 묶을 테이블 수는 2 이상이어야 한다.")
    void createErrorWhenTableNumberIsUnderTwo() {
        tableGroup.setOrderTables(Arrays.asList(orderTable1));
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("create Error: 그룹으로 묶을 테이블은 2개 이상이어야 합니다.");
    }

    @Test
    @DisplayName("그룹으로 묶을 테이블의 상태는 empty 여야 한다.")
    void createErrorWhenTableIsNotEmpty() {
        OrderTable nonEmptyTable = new OrderTable();
        nonEmptyTable.setEmpty(false);
        List<OrderTable> tables = Arrays.asList(orderTable1, nonEmptyTable);
        tableGroup.setOrderTables(tables);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(tables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("create Error: 테이블을 그룹으로 묶을 수 없습니다. (테이블이 비어있지 않거나, 이미 그룹이 되어있는 테이블)");
    }

    @Test
    @DisplayName("그룹으로 묶을 테이블은 다른 테이블 그룹이 아니어야 한다.")
    void createErrorWhenTableGroupIsInOtherTableGroup() {
        OrderTable alreadyGroupedTable = new OrderTable();
        alreadyGroupedTable.setEmpty(true);
        alreadyGroupedTable.setTableGroupId(2L);
        List<OrderTable> tables = Arrays.asList(orderTable1, alreadyGroupedTable);
        tableGroup.setOrderTables(tables);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(tables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("create Error: 테이블을 그룹으로 묶을 수 없습니다. (테이블이 비어있지 않거나, 이미 그룹이 되어있는 테이블)");
    }

    @Test
    void ungroup() {
        orderTable1.setTableGroupId(1L);
        orderTable2.setTableGroupId(1L);

        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(any());

        tableGroupService.ungroup(1L);

        assertThat(orderTables.get(0).isEmpty()).isEqualTo(false);
        assertThat(orderTables.get(1).isEmpty()).isEqualTo(false);
        assertThat(orderTables.get(0).getTableGroupId()).isNull();
        assertThat(orderTables.get(1).getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("해체할 테이블 그룹의 모든 테이블의 주문 상태는 결제 완료여야 한다.")
    void ungroupFail() {
        orderTable1.setTableGroupId(1L);
        orderTable2.setTableGroupId(1L);

        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("ungroup Error: 결제 완료여야 합니다.");
    }
}
