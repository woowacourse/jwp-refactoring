package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void createTableGroup() {
        final OrderTable orderTable1 = createEmptyTable();
        final OrderTable orderTable2 = createEmptyTable();
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        final TableGroup actual = tableGroupService.create(tableGroup);

        assertThat(actual.getId()).isNotNull();
    }

    @Test
    @DisplayName("주문 그룹이 비어있으면 예외 발생")
    void whenOrderTableIsEmpty() {
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), new ArrayList<>());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 그룹이 2개 미만이면 예외 발생")
    void whenOrderTableIsUnderTwo() {
        final OrderTable orderTable = createEmptyTable();
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블이 아닐 경우 예외 발생")
    void whenOrderTableIsNotEmptyTable() {
        final OrderTable orderTable1 = new OrderTable(null, 0, false);
        final OrderTable orderTable2 = createEmptyTable();
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 상태가 주문 테이블로 변경된다.")
    void createTableGroupIsChangeOrderTableStatus() {
        final OrderTable orderTable1 = createEmptyTable();
        final OrderTable orderTable2 = createEmptyTable();
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        final TableGroup actual = tableGroupService.create(tableGroup);
        final List<OrderTable> orderTables = actual.getOrderTables();

        assertAll(
                () -> assertThat(orderTables.get(0).isEmpty()).isFalse(),
                () -> assertThat(orderTables.get(1).isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("주문 테이블이 테이블 그룹에 포함되지 않을 경우 예외 발생")
    void whenOrderTableIsNotIncludeInTableGroup() {
        final OrderTable orderTable1 = createEmptyTable();
        final OrderTable orderTable2 = tableService.create(new OrderTable(null, 0, false));
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("각 주문 테이블이 기존 테이블 그룹에 포함되어 있는 경우 예외 발생")
    void whenOrderTableIsIncludeInExistTableGroup() {
        final OrderTable orderTable1 = createEmptyTable();
        final OrderTable orderTable2 = createEmptyTable();
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));
        final TableGroup persistedTableGroup = tableGroupService.create(tableGroup);

        final List<OrderTable> orderTables = persistedTableGroup.getOrderTables();
        final OrderTable orderTable3 = orderTables.get(0);
        orderTable3.setEmpty(true);
        orderTable3.setTableGroupId(persistedTableGroup.getId());
        orderTableDao.save(orderTable3);
        final TableGroup tableGroup2 = new TableGroup(LocalDateTime.now(), List.of(createEmptyTable(), orderTable3));


        assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다.")
    void ungroup() {
        final OrderTable orderTable1 = createEmptyTable();
        final OrderTable orderTable2 = createEmptyTable();
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        final TableGroup persistedTableGroup = tableGroupService.create(tableGroup);

        tableGroupService.ungroup(persistedTableGroup.getId());
        final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(
                List.of(orderTable1.getId(), orderTable2.getId()));

        assertAll(
                () -> assertThat(orderTables.get(0).getTableGroupId()).isNull(),
                () -> assertThat(orderTables.get(0).isEmpty()).isFalse(),
                () -> assertThat(orderTables.get(1).getTableGroupId()).isNull(),
                () -> assertThat(orderTables.get(1).isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("각 주문 테이블의 주문 상태가 완료상태가 아니면 예외 발생")
    void whenIsNotCompletion() {
        final OrderTable orderTable1 = createEmptyTable();
        final OrderTable orderTable2 = createEmptyTable();
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));
        final TableGroup persistedTableGroup = tableGroupService.create(tableGroup);

        orderService.create(
                new Order(orderTable1.getId(), null, LocalDateTime.now(), List.of(new OrderLineItem(null, 1L, 3))));

        assertThatThrownBy(() -> tableGroupService.ungroup(persistedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable createEmptyTable() {
        OrderTable orderTable = new OrderTable(null, 0, true);
        return tableService.create(orderTable);
    }
}
