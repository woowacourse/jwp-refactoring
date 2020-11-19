package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.Lists.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.inmemorydao.InMemoryOrderDao;
import kitchenpos.inmemorydao.InMemoryOrderTableDao;
import kitchenpos.inmemorydao.InMemoryTableGroupDao;

@DisplayName("TableGroupService 테스트")
class TableGroupServiceTest {
    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private TableGroupDao tableGroupDao;
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        this.orderDao = new InMemoryOrderDao();
        this.orderTableDao = new InMemoryOrderTableDao();
        this.tableGroupDao = new InMemoryTableGroupDao();
        this.tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @DisplayName("주문 테이블을 단체 지정한다")
    @Test
    void create() {
        // Given
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        // When
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(newArrayList(savedOrderTable1, savedOrderTable2));

        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // Then
        assertThat(savedTableGroup)
                .extracting(TableGroup::getId)
                .isNotNull()
        ;
    }

    @DisplayName("단체 지정할 주문 테이블의 목록이 비어있을 경우 예외가 발생한다")
    @Test
    void create_OrderTablesIsEmpty_ExceptionThrown() {
        // Given
        final TableGroup tableGroup = new TableGroup();

        // Then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @DisplayName("단체 지정할 주문 테이블의 수가 2보다 작을 경우 예외가 발생한다")
    @Test
    void create_OrderTablesSizeIsLowerThanTwo_ExceptionThrown() {
        // Given
        final OrderTable orderTable = new OrderTable();
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        // When
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(newArrayList(savedOrderTable));

        // Then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @DisplayName("주문 테이블이 존재하지 않을 경우 예외가 발생한다")
    @Test
    void create_OrderTableNotExists_ExceptionThrown() {
        // Given
        final OrderTable orderTable1 = new OrderTable();
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);

        // When
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(newArrayList(savedOrderTable1, orderTable2));

        // Then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @DisplayName("주문 테이블이 빈 테이블이 아닐 경우 예외가 발생한다")
    @Test
    void create_OrderTableIsNotEmpty_ExceptionThrown() {
        // Given
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(false);
        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        // When
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(newArrayList(savedOrderTable1, savedOrderTable2));

        // Then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @DisplayName("주문 테이블이 이미 단체 지정된 경우 예외가 발생한다")
    @Test
    void create_OrderTableAlreadyHasTableGroup_ExceptionThrown() {
        // Given
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setTableGroupId(1L);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        // When
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(newArrayList(savedOrderTable));

        // Then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @DisplayName("주문 테이블의 단체 지정을 해지한다")
    @Test
    void ungroup() {
        // Given
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(newArrayList(savedOrderTable1, savedOrderTable2));

        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // When
        tableGroupService.ungroup(savedTableGroup.getId());

        // Then
        final TableGroup ungroupedTableGroup = tableGroupDao.findById(savedTableGroup.getId())
                .orElseThrow(RuntimeException::new);

        assertAll(
                () -> assertThat(ungroupedTableGroup)
                        .extracting(TableGroup::getId)
                        .isEqualTo(savedTableGroup.getId())
                ,
                () -> assertThat(ungroupedTableGroup.getOrderTables())
                        .extracting(OrderTable::getTableGroupId)
                        .allMatch(orderTableId -> savedTableGroup.getId().equals(orderTableId))
        );
    }

    @DisplayName("단체 지정된 주문 테이블의 주문 상태가 조리 중 또는 식사 중인 경우 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void ungroup_OrderStatusIsCookingOrMeal_ExceptionThrown(final String orderStatus) {
        // Given
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        final Order order = new Order();
        order.setOrderTableId(savedOrderTable1.getId());
        order.setOrderStatus(orderStatus);
        orderDao.save(order);

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(newArrayList(savedOrderTable1, savedOrderTable2));

        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // Then
        final Long tableGroupId = savedTableGroup.getId();

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }
}
