package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

import kitchenpos.TestUtils;
import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
public class TableGroupServiceTest {

    private final TableGroupService tableGroupService;
    private final OrderTableDao orderTableDao;
    private final OrderDao orderDao;

    List<OrderTable> orderTables;

    public TableGroupServiceTest(TableGroupService tableGroupService, OrderTableDao orderTableDao, OrderDao orderDao) {
        this.tableGroupService = tableGroupService;
        this.orderTableDao = orderTableDao;
        this.orderDao = orderDao;
    }

    @BeforeEach
    void setUp() {
        OrderTable element1 = orderTableDao.save(new OrderTable(100, true));
        OrderTable element2 = orderTableDao.save(new OrderTable(100, true));
        orderTables = TestUtils.of(element1, element2);
    }


    @DisplayName("주문 테이블이 존재하지 않는 경우 예외가 발생한다.")
    @Test
    public void createWithEmptyOrderTable() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(TestUtils.of());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 개수가 1개인 경우 예외가 발생한다.")
    @Test
    public void createWithOneElementOrderTable() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(TestUtils.of(new OrderTable(50, true)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 사전에 저장되어 있지 않은 경우 예외가 발생한다.")
    public void createWithNoSavedOrderTable() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(TestUtils.of(
                new OrderTable(50, true),
                new OrderTable(50, true)
        ));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있지 않다면 예외가 발생한다.")
    @Test
    public void createWithNotEmptyOrderTable() {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> tempOrderTables = new ArrayList<>(orderTables);
        tempOrderTables.add(orderTableDao.save(new OrderTable(100, false)));
        tableGroup.setOrderTables(tempOrderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 저장된 경우 ID가 발급된다.")
    @Test
    public void create() {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> tempOrderTables = new ArrayList<>(orderTables);
        tableGroup.setOrderTables(tempOrderTables);

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        assertThat(savedTableGroup.getId()).isNotNull();
    }

    @DisplayName("그룹을 해제하는 경우를 테스트한다.")
    @Test
    public void ungroup() {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> tempOrderTables = new ArrayList<>(orderTables);
        OrderTable orderTable = orderTableDao.save(new OrderTable(100, true));
        tempOrderTables.add(orderTable);
        tableGroup.setOrderTables(tempOrderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        tableGroupService.ungroup(savedTableGroup.getId());
        assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isNull()
        );
    }

    @DisplayName("그룹을 해제하는 경우, 조리중이거나 식사 상태인 경우 예외가 발생한다.")
    @Test
    public void ungroupWithCookingAndMealState() {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> tempOrderTables = new ArrayList<>(orderTables);
        OrderTable orderTable = orderTableDao.save(new OrderTable(100, true));
        tempOrderTables.add(orderTable);
        tableGroup.setOrderTables(tempOrderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        Order order = new Order();
        order.setOrderTableId(orderTables.get(0).getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
