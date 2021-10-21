package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DisplayName("테이블그룹 서비스 테스트")
@MockitoSettings
class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    private List<OrderTable> orderTables;

    @BeforeEach
    void setUp() {
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);

        orderTables = Arrays.asList(orderTable1, orderTable2);
    }

    @DisplayName("새 테이블그룹을 생성한다. - 실패, 테이블그룹에 포함된 주문 테이블이 비어있는 경우")
    @Test
    void createFailedWhenOrderTablesEmpty() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(emptyList());

        // when - then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새 테이블그룹을 생성한다. - 실패, 테이블그룹에 포함된 주문 테이블이 2개 미만인 경우")
    @Test
    void createFailedWhenOrderTableLessThan2() {
        // given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);

        tableGroup.setOrderTables(singletonList(orderTable));

        // when - then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새 테이블그룹을 생성한다. - 실패, 입력으로 들어온 주문 테이블과 저장된 주문 테이블의 수가 다른 경우")
    @Test
    void createFailedWhenSizeNotEqual() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        List<OrderTable> savedOrderTables = singletonList(new OrderTable());
        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(savedOrderTables);

        // when - then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새 테이블그룹을 생성한다. - 실패, 비어있는 주문 테이블을 포함한 경우")
    @Test
    void createFailedWhenSavedOrderTableEmpty() {
        // given
        TableGroup tableGroup = new TableGroup();

        // 비어있는 orderTable을 포함한다.
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(1L);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setTableGroupId(1L);

        orderTables = Arrays.asList(orderTable1, orderTable2);
        tableGroup.setOrderTables(orderTables);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        List<OrderTable> savedOrderTables = orderTables;
        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(savedOrderTables);

        // when - then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새 테이블그룹을 생성한다. - 실패, 주문 테이블에 포함된 테이블그룹 Id가 null인 경우")
    @Test
    void createFailedWhenTableGroupIdNull() {
        // given
        TableGroup tableGroup = new TableGroup();

        // tableGroupId가 null임을 포함한다.
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setTableGroupId(null);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setTableGroupId(1L);

        orderTables = Arrays.asList(orderTable1, orderTable2);
        tableGroup.setOrderTables(orderTables);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        List<OrderTable> savedOrderTables = orderTables;
        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(savedOrderTables);

        // when - then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다. - 실패, 계산완료되지 않은 테이블을 포함한 경우")
    @Test
    void ungroupFailedWhenOrderStatusNotSatisfied() {
        // given
        long tableGroupId = 1L;
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setTableGroupId(1L);

        // meal 상태의 주문을 포함한다.
        Order order = new Order();
        order.setOrderStatus(OrderStatus.MEAL.name());
        order.setOrderTableId(1L);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setTableGroupId(1L);
        orderTables = Arrays.asList(orderTable1, orderTable2);

        given(orderTableDao.findAllByTableGroupId(tableGroupId)).willReturn(orderTables);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
        ).willThrow(IllegalArgumentException.class);

        // when - then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
