package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@DisplayName("TableGroup 서비스 테스트")
@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @DisplayName("테이블 그룹을 등록한다")
    @Test
    void create() {
        final TableGroup tableGroup = new TableGroup();

        final OrderTable orderTable1 = new OrderTable();
        final OrderTable orderTable2 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable2.setEmpty(true);
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertThat(savedTableGroup.getId()).isNotNull();
    }

    @DisplayName("테이블 그룹 등록 시 주문 테이블의 수가 2이상이어야 한다")
    @Test
    void createNumberOfOrderTableIsLowerTwo() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Collections.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록 시 등록하려는 테이블 그룹이 존재해야 한다")
    @Test
    void createOrderTableIsNotExist() {
        final TableGroup tableGroup = new TableGroup();

        final OrderTable notSavedOrderTable1 = new OrderTable();
        final OrderTable notSavedOrderTable2 = new OrderTable();
        final OrderTable notSavedOrderTable3 = new OrderTable();
        tableGroup.setOrderTables(List.of(notSavedOrderTable1, notSavedOrderTable2, notSavedOrderTable3));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록 시 주문 테이블이 비어있으면 안된다")
    @Test
    void createOrderTableIsNotEmpty() {
        final TableGroup tableGroup = new TableGroup();

        final OrderTable orderTable1 = new OrderTable();
        final OrderTable orderTable2 = new OrderTable();
        orderTable1.setEmpty(false);
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록 시 테이블 그룹의 아이디가 null 이어야 한다")
    @Test
    void createOrderTableIsNotNull() {
        final TableGroup tableGroup = new TableGroup();

        final OrderTable orderTable1 = new OrderTable();
        final OrderTable orderTable2 = new OrderTable();
        orderTable1.setTableGroupId(null);
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 그룹을 해제한다")
    @Test
    void ungroup() {
        // table group
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        final OrderTable orderTable1 = new OrderTable();
        final OrderTable orderTable2 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable2.setEmpty(true);
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
        tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        // order table
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(savedTableGroup.getId());
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        assertThatCode(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .doesNotThrowAnyException();
    }

    @DisplayName("테이블의 그룹을 해제할 때 테이블의 주문 상태가 요리중이거나 식사중일 경우 테이블을 비울 수 없다")
    @Test
    void ungroupOrderStatusIsCompletion() {
        // table group
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());

        final OrderTable orderTable1 = new OrderTable();
        final OrderTable orderTable2 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable2.setEmpty(true);
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
        tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        // order table
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(savedTableGroup.getId());
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
