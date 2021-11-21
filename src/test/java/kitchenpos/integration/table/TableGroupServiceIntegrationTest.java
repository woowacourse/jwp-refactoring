package kitchenpos.integration.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.integration.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceIntegrationTest extends IntegrationTest {

    private List<OrderTable> orderTables;

    @Autowired
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        OrderTable orderTable1 = new OrderTable(5L, null, 2, false);
        OrderTable orderTable2 = new OrderTable(6L, null, 2, false);

        orderTables = new ArrayList<>();
        orderTables.addAll(Arrays.asList(orderTable1, orderTable2));
    }

    @DisplayName("단체를 등록할 수 있다.")
    @Test
    void create_Valid_Success() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.add(orderTables);

        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup)
            .usingRecursiveComparison()
            .ignoringFields("id", "orderTables.tableGroupId", "orderTables.numberOfGuests")
            .isEqualTo(tableGroup);
    }

    @DisplayName("단체 지정 시 주문 테이블의 개수가 올바르지 않으면 등록할 수 없다.")
    @Test
    void create_InvalidOrderTables_Fail() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.add(Collections.emptyList());

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 주문 테이블이 하나라도 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_NonExistingOrderTables_Fail() {
        // given
        OrderTable orderTable = new OrderTable(15L, null, 0, true);

        orderTables.add(orderTable);

        TableGroup tableGroup = new TableGroup();
        tableGroup.add(orderTables);

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 이미 그룹화된 테이블이 하나라도 속해 있으면 등록할 수 없다.")
    @Test
    void create_AlreadyGrouping_Fail() {
        // given
        OrderTable orderTable = new OrderTable(4, false);

        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        orderTables.add(savedOrderTable);

        TableGroup tableGroup = new TableGroup();
        tableGroup.add(orderTables);

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체를 삭제할 수 있다.")
    @Test
    void ungroup_Valid_Success() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.add(orderTables);

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when
        // then
        assertThatCode(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .doesNotThrowAnyException();
    }

    @DisplayName("단체 해제 시 주문 상태가 `조리` 또는 `식사`면 삭제할 수 없다.")
    @Test
    void ungroup_InvalidOrderStatus_Fail() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.add(orderTables);

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        OrderTable orderTable = new OrderTable(savedTableGroup.getId(), 4, false);

        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(3L);
        orderLineItem.setQuantity(1);

        Order order = new Order(savedOrderTable.getId());
        order.add(Collections.singletonList(orderLineItem));

        orderService.create(order);

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
