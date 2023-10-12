package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kitchenpos.IntegrationTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends IntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuService menuService;

    @Test
    @DisplayName("단체 테이블 등록 시 지정된 빈 테이블들을 주문 테이블로 변경해 저장한다.")
    void 단체_테이블_지정_성공_주문_테이블로_변경() {
        // given
        final List<OrderTable> existingTables = tableService.list();
        final long emptyCountBefore = countEmpty(existingTables);

        // when
        final TableGroup saved = createTableGroup(existingTables);

        // then
        final long emptyCountAfter = countEmpty(saved.getOrderTables());
        assertThat(emptyCountBefore).isEqualTo(existingTables.size());
        assertThat(emptyCountAfter).isZero();
    }

    private TableGroup createTableGroup(final List<OrderTable> tables) {
        tables.forEach(table -> table.setEmpty(false));
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(tables);

        return tableGroupService.create(tableGroup);
    }

    private long countEmpty(final List<OrderTable> tables) {
        return tables.stream()
                .filter(OrderTable::isEmpty)
                .count();
    }

    @Test
    @DisplayName("기존에 존재하는 테이블만 단체로 지정할 수 있다.")
    void 단체_테이블_지정_실패_존재하지_않는_테이블() {
        // given
        final OrderTable existingTable = tableService.list().get(0);
        final OrderTable newTable = new OrderTable();
        newTable.setEmpty(true);

        // when
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(existingTable, newTable));

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("소속된 단체가 없는 테이블만 단체로 지정할 수 있다.")
    void 단체_테이블_지정_실패_이미_소속_단체가_있는_테이블() {
        // given
        final List<OrderTable> tablesWithGroup = tableService.list();
        createTableGroup(tablesWithGroup);

        // when
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(tablesWithGroup);

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블만 단체로 지정할 수 있다.")
    void 단체_테이블_지정_실패_주문_테이블() {
        // given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(createNotEmptyTables(2));

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private List<OrderTable> createNotEmptyTables(int count) {
        List<OrderTable> tables = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final OrderTable table = new OrderTable();
            table.setEmpty(false);
            tables.add(tableService.create(table));
        }
        return tables;
    }

    @Test
    @DisplayName("단체에 지정할 테이블은 2개 이상이어야 한다.")
    void 단체_테이블_지정_실패_주문_테이블_개수_미달() {
        // given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(createNotEmptyTables(1));

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블을 개별의 주문 테이블로 분할할 수 있다.")
    void 단체_테이블_분할_성공_저장() {
        // given
        final TableGroup tableGroup = createTableGroup(tableService.list());

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        final List<OrderTable> tablesAfterUngroup = tableService.list();
        assertAll(() -> assertThat(tablesAfterUngroup)
                        .map(OrderTable::getTableGroupId)
                        .allMatch(Objects::isNull),
                () -> assertThat(tablesAfterUngroup)
                        .noneMatch(OrderTable::isEmpty));
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    @DisplayName("주문 상태가 '조리', '식사'인 테이블이 있으면 분할할 수 없다.")
    void 단체_테이블_분할_실패_주문_상태(String orderStatus) {
        // given
        final List<OrderTable> tablesInGroup = tableService.list();
        final TableGroup tableGroup = createTableGroup(tablesInGroup);

        // when
        final OrderTable unableToSplit = tablesInGroup.get(0);
        placeOrder(unableToSplit, OrderStatus.valueOf(orderStatus));

        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void placeOrder(final OrderTable table, final OrderStatus status) {
        final Order order = new Order();
        order.setOrderStatus(status.name());
        order.setOrderTableId(table.getId());

        final OrderLineItem orderLineItem = new OrderLineItem();
        final Menu menuToOrder = menuService.list().get(0);
        orderLineItem.setMenuId(menuToOrder.getId());
        order.setOrderLineItems(List.of(orderLineItem));

        orderService.create(order);
    }

}
