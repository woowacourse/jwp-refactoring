package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Objects;
import kitchenpos.IntegrationTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends IntegrationTest {

    // TODO 너무 많은 의존성 해결하기
    @Autowired
    private TableService tableService;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuService menuService;

    @Test
    @DisplayName("주문 테이블 등록 시 전달받은 정보를 새 id로 저장한다.")
    void 주문_테이블_등록_성공_저장() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(5);

        // when
        final OrderTable saved = tableService.create(orderTable);

        // then
        assertThat(tableService.list())
                .map(OrderTable::getId)
                .filteredOn(id -> Objects.equals(id, saved.getId()))
                .hasSize(1);
    }

    @Test
    @DisplayName("모든 주문 테이블은 등록 시점에는 소속된 단체가 없다.")
    void 주문_테이블_등록_성공_단체_미지정() {
        // given
        final OrderTable orderTable = new OrderTable();

        // when
        final OrderTable saved = tableService.create(orderTable);

        // then
        assertThat(saved.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("특정 테이블을 빈 테이블로 변경하면 해당하는 id의 정보를 업데이트한다.")
    void 주문_테이블_비우기_성공_빈_테이블_여부() {
        // given
        final List<OrderTable> tables = tableService.list();
        final OrderTable table = tables.get(0);
        table.setEmpty(true);

        // when
        final OrderTable emptyTable = tableService.changeEmpty(table.getId(), table);

        // then
        assertThat(emptyTable.isEmpty()).isTrue();
        assertThat(tableService.list())
                .filteredOn(found -> Objects.equals(found.getId(), emptyTable.getId()))
                .filteredOn(OrderTable::isEmpty)
                .hasSize(1);
    }

    @Test
    @DisplayName("소속된 단체가 있으면 테이블을 비울 수 없다.")
    void 주문_테이블_비우기_실패_단체_소속() {
        // given
        final List<OrderTable> existingTables = tableService.list();
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(existingTables);
        tableGroupService.create(tableGroup);

        // when
        final OrderTable tableToEmpty = existingTables.get(0);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(tableToEmpty.getId(), tableToEmpty))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("조리 상태인 테이블을 비울 수 없다.")
    void 주문_테이블_비우기_실패_조리_상태() {
        // given
        final OrderTable tableToEmpty = tableService.list().get(0);
        tableToEmpty.setEmpty(false);
        tableService.changeEmpty(tableToEmpty.getId(), tableToEmpty);

        // when
        final Order order = new Order();
        order.setOrderStatus(COOKING.name());
        order.setOrderTableId(tableToEmpty.getId());
        final OrderLineItem orderLineItem = new OrderLineItem();
        final Menu menuToOrder = menuService.list().get(0);
        orderLineItem.setMenuId(menuToOrder.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        orderService.create(order);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(tableToEmpty.getId(), tableToEmpty))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("식사 상태인 테이블을 비울 수 없다.")
    void 주문_테이블_비우기_실패_식사_상태() {
        // given
        final OrderTable table = tableService.list().get(0);
        table.setEmpty(false);
        tableService.changeEmpty(table.getId(), table);

        // when
        final Order order = new Order();
        order.setOrderStatus(MEAL.name());
        order.setOrderTableId(table.getId());
        final OrderLineItem orderLineItem = new OrderLineItem();
        final Menu menuToOrder = menuService.list().get(0);
        orderLineItem.setMenuId(menuToOrder.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        orderService.create(order);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(table.getId(), table))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("특정 테이블의 방문한 손님 수를 변경할 수 있다.")
    void 주문_테이블_방문한_손님수_변경_성공() {
        // given
        final OrderTable table = tableService.list().get(0);
        table.setEmpty(false);
        tableService.changeEmpty(table.getId(), table);

        // when
        final int numberOfGuests = 10;
        table.setNumberOfGuests(numberOfGuests);
        tableService.changeNumberOfGuests(table.getId(), table);

        // then
        assertThat(table.getNumberOfGuests()).isEqualTo(numberOfGuests);
        assertThat(tableService.list())
                .filteredOn(found -> Objects.equals(found.getId(), table.getId()))
                .filteredOn(found -> Objects.equals(found.getNumberOfGuests(), numberOfGuests))
                .hasSize(1);
    }

    @Test
    @DisplayName("특정 테이블의 방문한 손님 수는 0 이상이어야 한다.")
    void 주문_테이블_방문한_손님_수_변경_실패_음수() {
        // given
        final OrderTable table = tableService.list().get(0);
        table.setEmpty(false);
        tableService.changeEmpty(table.getId(), table);

        // when
        table.setNumberOfGuests(-10);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(table.getId(), table))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블의 방문한 손님 수를 변경할 수 없다.")
    void 주문_테이블_방문한_손님_수_변경_실패_빈_테이블() {
        // given
        final OrderTable table = tableService.list().get(0);

        // when
        table.setNumberOfGuests(10);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(table.getId(), table))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
