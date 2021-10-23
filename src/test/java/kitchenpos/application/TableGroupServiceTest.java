package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정")
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

    @DisplayName("단체 지정을 등록할 수 있다")
    @Test
    void create() {
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(null);
        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(null);
        final List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        final List<Long> orderTableIds = Arrays.asList(1L, 2L);
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        final TableGroup savedTableGroup = new TableGroup();
        savedTableGroup.setId(1L);
        savedTableGroup.setOrderTables(orderTables);

        when(orderTableDao.findAllByIdIn(orderTableIds)).thenReturn(orderTables);
        when(tableGroupDao.save(tableGroup)).thenReturn(savedTableGroup);
        when(orderTableDao.save(orderTable1)).thenReturn(orderTable1);
        when(orderTableDao.save(orderTable2)).thenReturn(orderTable2);

        final TableGroup actual = tableGroupService.create(tableGroup);
        assertThat(actual).isEqualTo(savedTableGroup);
    }

    @DisplayName("주문 테이블이 비어있으면 안 된다")
    @Test
    void createExceptionEmpty() {
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(false);
        orderTable1.setTableGroupId(null);
        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(null);
        final List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 목록이 2 보다 작으면 안 된다")
    @Test
    void createExceptionUnderTwo() {
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(null);
        final List<OrderTable> orderTables = Collections.singletonList(orderTable1);
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블로 등록되어 있는 목록과 단체 주문에 있는 주문 테이블 목록과 크기가 같아야 한다")
    @Test
    void createExceptionTablesSize() {
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(null);
        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(null);
        final OrderTable orderTable3 = new OrderTable();
        orderTable3.setId(3L);
        orderTable3.setEmpty(true);
        orderTable3.setTableGroupId(null);
        final List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        final List<OrderTable> savedOrderTables = Arrays.asList(orderTable1, orderTable2, orderTable3);
        final List<Long> orderTableIds = Arrays.asList(1L, 2L);
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        when(orderTableDao.findAllByIdIn(orderTableIds)).thenReturn(savedOrderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되어 있는 주문 테이블이 비어 있지 않으면 예외가 발생한다")
    @Test
    void createExceptionSavedEmpty() {
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(null);
        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(null);
        final List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        final List<Long> orderTableIds = Arrays.asList(1L, 2L);
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        final OrderTable savedOrderTable1 = new OrderTable();
        savedOrderTable1.setId(1L);
        savedOrderTable1.setEmpty(false);
        savedOrderTable1.setTableGroupId(1L);
        final OrderTable savedOrderTable2 = new OrderTable();
        savedOrderTable2.setId(2L);
        savedOrderTable2.setEmpty(true);
        savedOrderTable2.setTableGroupId(1L);
        final List<OrderTable> savedOrderTables = Arrays.asList(savedOrderTable1, savedOrderTable2);

        when(orderTableDao.findAllByIdIn(orderTableIds)).thenReturn(savedOrderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되어 있는 주문 테이블의 그룹 지정이 없으면 예외를 발생한다")
    @Test
    void createExceptionEmptyAndGroup() {
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(null);
        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(null);
        final List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        final List<Long> orderTableIds = Arrays.asList(1L, 2L);
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        final OrderTable savedOrderTable1 = new OrderTable();
        savedOrderTable1.setId(1L);
        savedOrderTable1.setEmpty(true);
        savedOrderTable1.setTableGroupId(null);
        final OrderTable savedOrderTable2 = new OrderTable();
        savedOrderTable2.setId(2L);
        savedOrderTable2.setEmpty(true);
        savedOrderTable2.setTableGroupId(1L);
        final List<OrderTable> savedOrderTables = Arrays.asList(savedOrderTable1, savedOrderTable2);

        when(orderTableDao.findAllByIdIn(orderTableIds)).thenReturn(savedOrderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제할 수 있다")
    @Test
    void ungroup() {
        final Long tableGroupId = 1L;
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(null);
        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(null);
        final List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        when(orderTableDao.findAllByTableGroupId(tableGroupId)).thenReturn(orderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);

        assertThatCode(() -> tableGroupService.ungroup(tableGroupId)).doesNotThrowAnyException();
    }

    @DisplayName("단체 지정의 주문 테이블의 주문이 있고, 주문 상태가 `COOKING`, `MEAL`이라면 예외가 발생한다")
    @Test
    void ungroupExceptionExistsAndStatus() {
        final Long tableGroupId = 1L;
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(null);
        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(null);
        final List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        when(orderTableDao.findAllByTableGroupId(tableGroupId)).thenReturn(orderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId)).isInstanceOf(IllegalArgumentException.class);
    }
}
