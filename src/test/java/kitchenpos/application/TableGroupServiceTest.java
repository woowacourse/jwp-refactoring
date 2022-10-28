package kitchenpos.application;

import static kitchenpos.fixture.MenuFactory.menu;
import static kitchenpos.fixture.MenuGroupFactory.menuGroup;
import static kitchenpos.fixture.OrderFactory.order;
import static kitchenpos.fixture.OrderTableFactory.emptyTable;
import static kitchenpos.fixture.OrderTableFactory.notEmptyTable;
import static kitchenpos.fixture.ProductFactory.product;
import static kitchenpos.fixture.TableGroupFactory.tableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends FakeSpringContext {

    private final TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

    @DisplayName("테이블 그룹 등록")
    @Test
    void create() {
        final var singleTable = orderTableDao.save(emptyTable(1));
        final var doubleTable = orderTableDao.save(emptyTable(2));

        final var tableGroup = tableGroup(singleTable, doubleTable);

        final var result = tableGroupService.create(tableGroup);
        assertThat(result.getOrderTables().size()).isEqualTo(tableGroup.getOrderTables().size());
    }

    @DisplayName("주문 테이블 중 하나라도 빈 상태가 아니라면, 테이블 그룹 등록 시 예외 발생")
    @Test
    void create_containsNotEmptyTable_throwsException() {
        final var notEmptyTable = orderTableDao.save(notEmptyTable(2));
        final var emptyTable = orderTableDao.save(emptyTable(2));

        final var tableGroup = tableGroup(notEmptyTable, emptyTable);

        assertThatThrownBy(
                () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 중 하나라도 이미 단체 id를 갖고 있다면, 테이블 그룹 등록 시 예외 발생")
    @Test
    void create_containsAlreadyGroupedTable_throwsException() {
        final var singleTable = orderTableDao.save(emptyTable(1));
        final var coupleTable = orderTableDao.save(emptyTable(2));
        final var tripleTable = orderTableDao.save(emptyTable(3));

        createTableGroupAndSave(singleTable, coupleTable);

        final var tableGroup = tableGroup(singleTable, tripleTable);

        assertThatThrownBy(
                () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() {
        final var singleTable = orderTableDao.save(emptyTable(1));
        final var doubleTable = orderTableDao.save(emptyTable(2));

        final var tableGroup = createTableGroupAndSave(singleTable, doubleTable);

        tableGroupService.ungroup(tableGroup.getId());

        final var ungroupedTables = orderTableDao.findAllByIdIn(
                List.of(singleTable.getId(), doubleTable.getId()));
        assertAll(
                () -> assertThat(ungroupedTables).allMatch(table -> table.getTableGroupId() == null),
                () -> assertThat(ungroupedTables).allMatch(table -> !table.isEmpty())
        );
    }

    @DisplayName("그룹의 주문 테이블 중 하나라도 COMPLETION 상태가 아니라면 해제 시 예외 발생")
    @Test
    void ungroup_containsNotCompletionStatus_throwsException() {
        final var pizza = productDao.save(product("피자", 10_000));
        final var coke = productDao.save(product("콜라", 1_000));

        final var italian = menuGroupDao.save(menuGroup("양식"));

        final var pizzaMenu = menuDao.save(menu("피자파티", italian, List.of(pizza)));
        final var cokeMenu = menuDao.save(menu("콜라파티", italian, List.of(coke)));

        final var singleTable = orderTableDao.save(emptyTable(1));
        final var doubleTable = orderTableDao.save(emptyTable(2));

        orderDao.save(order(singleTable, OrderStatus.MEAL, pizzaMenu));
        orderDao.save(order(doubleTable, OrderStatus.COMPLETION, cokeMenu));

        final var tableGroup = createTableGroupAndSave(singleTable, doubleTable);

        assertThatThrownBy(
                () -> tableGroupService.ungroup(tableGroup.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private TableGroup createTableGroupAndSave(final OrderTable... orderTables) {
        final var group = tableGroupDao.save(tableGroup(orderTables));
        for (OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(group.getId());
            orderTableDao.save(orderTable);
        }
        return group;
    }
}
