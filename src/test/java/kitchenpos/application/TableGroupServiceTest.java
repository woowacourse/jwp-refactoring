package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    void create() {
        OrderTable firstTable = new OrderTable();
        firstTable.setId(1L);
        firstTable.setEmpty(true);
        OrderTable secondTable = new OrderTable();
        secondTable.setId(2L);
        secondTable.setEmpty(true);
        List<OrderTable> orderTables = Arrays.asList(firstTable, secondTable);
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(3L);
        tableGroup.setOrderTables(orderTables);

        given(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(orderTables);
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(tableGroup);

        TableGroup result = tableGroupService.create(tableGroup);

        assertThat(result).isNotNull();
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(3L),
            () -> assertThat(result.getOrderTables()).hasSize(2),
            () -> assertThat(result.getOrderTables().get(0).getId()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("주문 테이블이 비어있거나 2 미만인 경우")
    void orderTablesBelowTwo() {
        TableGroup tableGroup = new TableGroup();
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);

        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(true);
        tableGroup.setOrderTables(Collections.singletonList(orderTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("그룹내의 주문 테이블과 등록된 주문 테이블에서 조회한 것이 다른 경우")
    void differentCountOfOrderTableAndRegisterOrderTable() {
        OrderTable firstTable = new OrderTable();
        firstTable.setId(1L);
        firstTable.setEmpty(true);
        OrderTable secondTable = new OrderTable();
        secondTable.setId(2L);
        secondTable.setEmpty(true);
        List<OrderTable> orderTables = Arrays.asList(firstTable, secondTable);
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(3L);
        tableGroup.setOrderTables(orderTables);

        given(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(Collections.singletonList(firstTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("그룹내의 주문 테이블이 비어있지 않은 경우")
    void notEmptyOrderTable() {
        OrderTable firstTable = new OrderTable();
        firstTable.setId(1L);
        firstTable.setEmpty(false);
        OrderTable secondTable = new OrderTable();
        secondTable.setId(2L);
        secondTable.setEmpty(true);
        List<OrderTable> orderTables = Arrays.asList(firstTable, secondTable);
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(3L);
        tableGroup.setOrderTables(orderTables);

        given(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(Collections.singletonList(firstTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orderTableBelongToOtherTableGroup() {
        OrderTable firstTable = new OrderTable();
        firstTable.setId(1L);
        firstTable.setEmpty(false);
        firstTable.setTableGroupId(3L);
        OrderTable secondTable = new OrderTable();
        secondTable.setId(2L);
        secondTable.setEmpty(true);
        List<OrderTable> orderTables = Arrays.asList(firstTable, secondTable);
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(3L);
        tableGroup.setOrderTables(orderTables);

        given(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(Collections.singletonList(firstTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("그룹 해제")
    void ungroup() {
        OrderTable firstTable = new OrderTable();
        firstTable.setId(1L);
        firstTable.setEmpty(true);
        OrderTable secondTable = new OrderTable();
        secondTable.setId(2L);
        secondTable.setEmpty(true);
        List<OrderTable> orderTables = Arrays.asList(firstTable, secondTable);

        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        assertDoesNotThrow(() -> tableGroupService.ungroup(1L));
    }

    @Test
    @DisplayName("주문이 완료되지 않은 경우")
    void orderNotCompleted() {
        OrderTable firstTable = new OrderTable();
        firstTable.setId(1L);
        firstTable.setEmpty(true);
        OrderTable secondTable = new OrderTable();
        secondTable.setId(2L);
        secondTable.setEmpty(true);
        List<OrderTable> orderTables = Arrays.asList(firstTable, secondTable);

        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
