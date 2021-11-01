package kitchenpos.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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

    private OrderTable orderTable;
    private OrderTable orderTable2;

    List<OrderTable> orderTables = new ArrayList<>();

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable2 = new OrderTable();
        orderTable2.setId(2L);

        tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
    }

    @Test
    @DisplayName("테이블 그룹을 지정한다.")
    void createTableGroup() {
        // given
        orderTables.addAll(Arrays.asList(orderTable, orderTable2));
        final List<Long> orderTableIds = this.orderTables.stream()
                                                         .map(OrderTable::getId)
                                                         .collect(Collectors.toList());

        orderTable.setEmpty(true);
        orderTable2.setEmpty(true);

        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(this.orderTables);
        given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);

        // when, then
        assertDoesNotThrow(() -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("테이블 그룹의 주문 테이블이 비어 있거나, 총 테이블의 수가 2보다 작으면 예외를 발생시킨다.")
    void throwExceptionWhenInvalidTableSize() {
        // given
        orderTables = Collections.singletonList(orderTable);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블이 포함되어 있으면 예외를 발생시킨다.")
    void throwExceptionWhenTableNotExists() {
        // given
        orderTables.addAll(Arrays.asList(orderTable, orderTable2));
        given(orderTableDao.findAllByIdIn(any())).willReturn(Collections.singletonList(orderTable));

        // when, then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("주문 테이블들이 하나라도 비어있지 않으면 예외를 발생시킨다.")
    void throwExceptionWhenTableNotEmpty() {
        // given
        orderTable.setEmpty(false);
        orderTables.addAll(Arrays.asList(orderTable, orderTable2));
        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("주문 테이블들이 하나라도 그룹 지정이 되어 있다면 예외를 발생시킨다.")
    void throwExceptionWhenTableGrouped() {
        // given
        orderTable.setEmpty(true);
        orderTable.setTableGroupId(1L);
        orderTable2.setEmpty(true);

        orderTables.addAll(Arrays.asList(orderTable, orderTable2));
        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("테이블들의 단체 지정을 해제한다.")
    void ungroupTable() {
        // given
        orderTable.setTableGroupId(1L);
        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(Collections.singletonList(orderTable));
        given(
                orderDao.existsByOrderTableIdInAndOrderStatusIn(
                        Collections.singletonList(1L),
                        Arrays.asList(OrderStatus.COOKING.name(),
                        OrderStatus.MEAL.name())
                )
        ).willReturn(false);

        // when, then
        assertDoesNotThrow(() -> tableGroupService.ungroup(1L));
    }

    @Test
    @DisplayName("테이블들의 단체 지정을 해제한다.")
    void throwExceptionWhenUngroupTableNotCompleted() {
        // given
        orderTable.setTableGroupId(1L);
        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(Collections.singletonList(orderTable));
        given(
                orderDao.existsByOrderTableIdInAndOrderStatusIn(
                        Collections.singletonList(1L),
                        Arrays.asList(OrderStatus.COOKING.name(),
                        OrderStatus.MEAL.name())
                )
        ).willReturn(true);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.ungroup(1L));
    }
}