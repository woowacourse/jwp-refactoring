package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
public class TableGroupServiceTest {

    private final TableGroupService tableGroupService;
    private final OrderTableDao orderTableDao;
    private final OrderDao orderDao;

    OrderTable orderTable1;
    OrderTable orderTable2;

    public TableGroupServiceTest(TableGroupService tableGroupService, OrderTableDao orderTableDao, OrderDao orderDao) {
        this.tableGroupService = tableGroupService;
        this.orderTableDao = orderTableDao;
        this.orderDao = orderDao;
    }

    @BeforeEach
    void setUp() {
        orderTable1 = orderTableDao.save(new OrderTable(100, true));
        orderTable2 = orderTableDao.save(new OrderTable(100, true));
    }

    @DisplayName("단체 지정 시 주문 테이블이 존재하지 않는 경우 예외가 발생한다.")
    @Test
    public void createWithEmptyOrderTable() {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Collections.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 주문 테이블의 개수가 1개인 경우 예외가 발생한다.")
    @Test
    public void createWithOneElementOrderTable() {
        TableGroup tableGroup = new TableGroup(
                LocalDateTime.now(), List.of(orderTable1)
        );

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 주문 테이블이 사전에 저장되어 있지 않은 경우 예외가 발생한다.")
    @Test
    public void createWithNoSavedOrderTable() {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                List.of(new OrderTable(50, true), new OrderTable(50, true))
        );

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 주문 테이블이 비어있지 않다면 예외가 발생한다.")
    @Test
    public void createWithNotEmptyOrderTable() {
        List<OrderTable> tempOrderTables = new ArrayList<>(List.of(orderTable1, orderTable2));
        tempOrderTables.add(orderTableDao.save(new OrderTable(100, false)));
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), tempOrderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 단체가 지정된 경우 ID가 발급된다.")
    @Test
    public void create() {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        assertThat(tableGroupService.create(tableGroup).getId()).isNotNull();
    }

    @DisplayName("그룹을 해제하는 경우를 테스트한다.")
    @Test
    public void ungroup() {
        List<OrderTable> tempOrderTables = List.of(orderTable1, orderTable2);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), tempOrderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        tableGroupService.ungroup(savedTableGroup.getId());

        OrderTable orderTable = orderTableDao.findById(orderTable1.getId()).get();

        assertThat(orderTable.getTableGroupId()).isNull();
    }

    @DisplayName("그룹을 해제하는 경우, 조리중이거나 식사 상태인 경우 예외가 발생한다.")
    @Test
    public void ungroupWithCookingAndMealState() {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        Order order = new Order(
                orderTable1.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Collections.emptyList()
        );
        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
