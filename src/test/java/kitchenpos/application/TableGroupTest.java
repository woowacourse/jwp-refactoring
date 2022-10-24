package kitchenpos.application;

import static kitchenpos.application.OrderFixture.ORDER_LINE_ITEMS;
import static kitchenpos.application.TableGroupFixture.SAVED_TABLE_GROUP;
import static kitchenpos.application.TableGroupFixture.UNSAVED_TABLE_GROUP;
import static kitchenpos.application.TableGroupFixture.UNSAVED_TABLE_GROUP_INVALID_INCLUDE_NOT_EMPTY_TABLE;
import static kitchenpos.application.TableGroupFixture.UNSAVED_TABLE_GROUP_INVALID_TOO_LITTLE_TABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @DisplayName("테이블 그룹을 생성한다. 테이블 그룹에 속한 테이블은 모두 empty가 아니게 된다.")
    @Test
    void create() {
        TableGroup savedTableGroup = tableGroupService.create(UNSAVED_TABLE_GROUP);
        List<Boolean> tableEmptyFlags = savedTableGroup.getOrderTables().stream()
                .map(OrderTable::isEmpty)
                .collect(Collectors.toList());

        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(tableEmptyFlags).allMatch(emptyFlag -> emptyFlag.equals(false));
    }

    @DisplayName("테이블 그룹에 속하려는 테이블이 empty가 아니면 예외가 발생한다.")
    @Test
    void create_Exception_Table_Not_empty() {
        assertThatThrownBy(() -> tableGroupService.create(UNSAVED_TABLE_GROUP_INVALID_INCLUDE_NOT_EMPTY_TABLE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹에 속하려는 테이블은 적어도 2개 이상이여야 한다. 아니면 예외가 발생한다.")
    @Test
    void create_Exception_Table_Too_Little() {
        assertThatThrownBy(() -> tableGroupService.create(UNSAVED_TABLE_GROUP_INVALID_TOO_LITTLE_TABLE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        Order completionOrder = mock(Order.class);
        when(completionOrder.getOrderStatus()).thenReturn(OrderStatus.COMPLETION.name());

        List<OrderTable> orderTables = SAVED_TABLE_GROUP.getOrderTables();
        OrderTable orderTable = orderTables.get(0);
        Order order = new Order(orderTable.getId(), ORDER_LINE_ITEMS);
        Order savedOrder = orderService.create(order);
        orderService.changeOrderStatus(savedOrder.getId(), completionOrder);
        tableGroupService.ungroup(SAVED_TABLE_GROUP.getId());

        List<OrderTable> ungroupOrderTables = SAVED_TABLE_GROUP.getOrderTables();
        for (OrderTable ungroupOrderTable : ungroupOrderTables) {
            assertThat(ungroupOrderTable.getTableGroupId()).isNull();
            assertThat(ungroupOrderTable.isEmpty()).isFalse();
        }
    }

    @DisplayName("테이블 그룹에 속해있는 주문이 모두 완료상태가 아니면 예외가 발생한다.")
    @Test
    void ungroup_Exception_Not_Completion_Order() {
        List<OrderTable> orderTables = SAVED_TABLE_GROUP.getOrderTables();
        OrderTable orderTable = orderTables.get(0);
        Order order = new Order(orderTable.getId(), ORDER_LINE_ITEMS);
        orderService.create(order);
        assertThatThrownBy(() -> tableGroupService.ungroup(SAVED_TABLE_GROUP.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
