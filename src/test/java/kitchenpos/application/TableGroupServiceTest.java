package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    protected TableGroupService tableGroupService;
    @Autowired
    protected OrderDao orderDao;
    @Autowired
    protected OrderTableDao orderTableDao;
    @Autowired
    protected TableGroupDao tableGroupDao;

    @Test
    @DisplayName("주문 테이블을 단체 지정한다")
    void create() {
        // given
        OrderTable createdOrderTable = createEmptyOrderTable();
        OrderTable createdOrderTable2 = createEmptyOrderTable();

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(createdOrderTable, createdOrderTable2));
        tableGroup.setCreatedDate(LocalDateTime.now());

        // when
        TableGroup createdTableGroup = tableGroupService.create(tableGroup);
        OrderTable firstTable = createdTableGroup.getOrderTables().get(0);

        // then
        assertAll(
            () -> assertThat(createdTableGroup.getOrderTables()).hasSameSizeAs(tableGroup.getOrderTables()),
            () -> assertThat(firstTable.getTableGroupId()).isEqualTo(createdTableGroup.getId()),
            () -> assertThat(firstTable.getNumberOfGuests()).isEqualTo(createdOrderTable.getNumberOfGuests())
        );
    }

    @Test
    @DisplayName("테이블의 목록을 설정하지 않고 단체 지정할 수 없다")
    void nonRegisteredOrderTables() {
        // given
        TableGroup tableGroup = new TableGroup();

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("2개 미만의 테이블로 단체 지정할 수 없다")
    void lessThan2Tables() {
        // given
        OrderTable createdOrderTable = createEmptyOrderTable();

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(createdOrderTable));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록되지 않은 테이블을 포함해서 단체 지정할 수 없다")
    void includeNonRegisteredTable() {
        // given
        OrderTable createdOrderTable = createEmptyOrderTable();

        Long fakeOrderTableId = 999L;
        OrderTable createdOrderTable2 = new OrderTable();
        createdOrderTable2.setId(fakeOrderTableId);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(createdOrderTable, createdOrderTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("점유된 테이블을 포함해서 단체 지정할 수 없다")
    void includeNotEmptyTable() {
        // given
        OrderTable createdOrderTable = createEmptyOrderTable();

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        OrderTable createdOrderTable2 = orderTableDao.save(orderTable);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(createdOrderTable, createdOrderTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 단체 지정된 테이블을 포함해서 단체 지정할 수 없다")
    void includeAlreadyGroupedTable() {
        // given
        OrderTable createdOrderTable = createEmptyOrderTable();
        OrderTable createdOrderTable2 = createEmptyOrderTable();

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(createdOrderTable, createdOrderTable2));
        tableGroup.setCreatedDate(LocalDateTime.now());

        // when
        createTableGroup(List.of(createdOrderTable, createdOrderTable2));

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 해제한다")
    void ungroup() {
        // given
        OrderTable createdOrderTable = createEmptyOrderTable();
        OrderTable createdOrderTable2 = createEmptyOrderTable();

        TableGroup createdTableGroup = createTableGroup(List.of(createdOrderTable, createdOrderTable2));

        // when
        tableGroupService.ungroup(createdTableGroup.getId());

        // then
        TableGroup tableGroup = tableGroupDao.findById(createdTableGroup.getId()).get();
        List<OrderTable> tables = orderTableDao.findAllByIdIn(
            Stream.of(createdOrderTable, createdOrderTable2)
                .map(OrderTable::getId)
                .collect(Collectors.toList())
        );

        assertAll(
            () -> assertThat(tableGroup.getId()).isEqualTo(createdTableGroup.getId()),
            () -> assertThat(tableGroup.getOrderTables()).isNull(),
            () -> assertThat(
                tables.stream().map(OrderTable::getTableGroupId)
            ).containsOnly((Long)null)
        );
    }

    @Test
    @DisplayName("완료되지 않은 상태의 테이블이 있으면 단체 지정을 해제할 수 없다")
    void includeInCompletedTable() {
        // given
        OrderTable createdOrderTable = createEmptyOrderTable();
        OrderTable createdOrderTable2 = createEmptyOrderTable();

        Order order = new Order();
        order.setOrderTableId(createdOrderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        TableGroup createdTableGroup = createTableGroup(List.of(createdOrderTable, createdOrderTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(createdTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private TableGroup createTableGroup(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        tableGroup.setCreatedDate(LocalDateTime.now());

        TableGroup createdTableGroup = tableGroupDao.save(tableGroup);

        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            orderTable.setTableGroupId(createdTableGroup.getId());
            orderTableDao.save(orderTable);
        }
        return createdTableGroup;
    }

    private OrderTable createEmptyOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        return orderTableDao.save(orderTable);
    }
}
