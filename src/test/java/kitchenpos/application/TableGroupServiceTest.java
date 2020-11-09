package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
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

    @DisplayName("테이블 그룹을 설정한다.")
    @Test
    void create() {
        TableGroup tableGroup = new TableGroup();

        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();

        orderTable1.setId(1L);
        orderTable2.setId(2L);

        OrderTable savedTable1 = new OrderTable();
        OrderTable savedTable2 = new OrderTable();

        savedTable1.setId(1L);
        savedTable1.setEmpty(true);

        savedTable2.setId(2L);
        savedTable2.setEmpty(true);

        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        TableGroup savedTableGroup = new TableGroup();
        savedTableGroup.setId(1L);
        savedTableGroup.setCreatedDate(LocalDateTime.now());

        given(orderTableDao.findAllByIdIn(
            Stream.of(savedTable1, savedTable2).map(OrderTable::getId).collect(Collectors.toList()))).willReturn(
            Arrays.asList(savedTable1, savedTable2));
        given(tableGroupDao.save(tableGroup)).willReturn(savedTableGroup);

        TableGroup expected = tableGroupService.create(tableGroup);

        assertAll(
            () -> assertThat(expected).extracting(TableGroup::getId).isEqualTo(savedTableGroup.getId()),
            () -> assertThat(expected).extracting(TableGroup::getCreatedDate).isNotNull(),
            () -> assertThat(expected).extracting(TableGroup::getOrderTables,
                InstanceOfAssertFactories.list(OrderTable.class))
                .extracting(OrderTable::getTableGroupId)
                .containsOnly(savedTableGroup.getId()),
            () -> assertThat(expected).extracting(TableGroup::getOrderTables,
                InstanceOfAssertFactories.list(OrderTable.class))
                .extracting(OrderTable::isEmpty)
                .containsOnly(false)
        );
    }

    @DisplayName("테이블 그룹 생성 시 테이블이 2개 미만일 경우 예외 처리한다.")
    @Test
    void createWithEmptyOrOneTable() {
        TableGroup tableGroup = new TableGroup();

        OrderTable orderTable1 = new OrderTable();

        orderTable1.setId(1L);

        tableGroup.setOrderTables(Collections.singletonList(orderTable1));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 생성할 시 TableId가 존재하지 않을 경우 예외 처리한다.")
    @Test
    void createWithNotExistingTableId() {
        TableGroup tableGroup = new TableGroup();

        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();

        orderTable1.setId(1L);
        orderTable2.setId(2L);

        OrderTable savedTable1 = new OrderTable();
        OrderTable savedTable2 = new OrderTable();

        savedTable1.setId(1L);
        savedTable1.setEmpty(true);

        savedTable2.setId(2L);
        savedTable2.setEmpty(true);

        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(
            Stream.of(savedTable1, savedTable2).map(OrderTable::getId).collect(Collectors.toList()))).willReturn(
            Collections.singletonList(savedTable1));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 생성 시 TableGroup이 이미 지정되어 있으면 예외 처리한다.")
    @Test
    void createWithExistingTableGroup() {
        TableGroup tableGroup = new TableGroup();

        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();

        orderTable1.setId(1L);
        orderTable2.setId(2L);

        OrderTable savedTable1 = new OrderTable();
        OrderTable savedTable2 = new OrderTable();

        savedTable1.setId(1L);
        savedTable1.setTableGroupId(1L);
        savedTable1.setEmpty(true);

        savedTable2.setId(2L);
        savedTable2.setTableGroupId(1L);
        savedTable2.setEmpty(true);

        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(
            Stream.of(savedTable1, savedTable2).map(OrderTable::getId).collect(Collectors.toList()))).willReturn(
            Arrays.asList(savedTable1, savedTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 생성 시 TableGroup이 비어있지 않다면 예외 처리한다.")
    @Test
    void createWithNotEmpty() {
        TableGroup tableGroup = new TableGroup();

        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();

        orderTable1.setId(1L);
        orderTable2.setId(2L);

        OrderTable savedTable1 = new OrderTable();
        OrderTable savedTable2 = new OrderTable();

        savedTable1.setId(1L);
        savedTable1.setEmpty(false);

        savedTable2.setId(2L);
        savedTable2.setEmpty(false);

        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(
            Stream.of(savedTable1, savedTable2).map(OrderTable::getId).collect(Collectors.toList()))).willReturn(
            Arrays.asList(savedTable1, savedTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        Long tableGroupId = 1L;

        OrderTable savedTable1 = new OrderTable();
        OrderTable savedTable2 = new OrderTable();

        savedTable1.setId(1L);
        savedTable1.setTableGroupId(tableGroupId);
        savedTable1.setEmpty(false);

        savedTable2.setId(2L);
        savedTable2.setTableGroupId(tableGroupId);
        savedTable2.setEmpty(false);

        List<OrderTable> savedTables = Arrays.asList(savedTable1, savedTable2);
        List<Long> ids = savedTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(savedTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
            ids, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        tableGroupService.ungroup(tableGroupId);

        assertAll(
            () -> verify(orderTableDao, times(ids.size())).save(any(OrderTable.class)),
            () -> assertThat(savedTables).extracting(OrderTable::getTableGroupId).containsOnlyNulls()
        );
    }

    @DisplayName("테이블 그룹 해제 시 완료되지 않은 주문이 있을 경우 예외 처리한다.")
    @Test
    void ungroupWithNotCompletedOrder() {
        Long tableGroupId = 1L;

        OrderTable savedTable1 = new OrderTable();
        OrderTable savedTable2 = new OrderTable();

        savedTable1.setId(1L);
        savedTable1.setTableGroupId(tableGroupId);
        savedTable1.setEmpty(false);

        savedTable2.setId(2L);
        savedTable2.setTableGroupId(tableGroupId);
        savedTable2.setEmpty(false);

        List<OrderTable> savedTables = Arrays.asList(savedTable1, savedTable2);
        List<Long> ids = savedTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(savedTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
            ids, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
            .isInstanceOf(IllegalArgumentException.class);
    }
}