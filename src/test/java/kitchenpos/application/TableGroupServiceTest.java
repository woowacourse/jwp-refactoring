package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.utils.TestObjectFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql({"/truncate.sql", "/init-data.sql"})
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderService orderService;

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create() {
        OrderTable orderTableA = TestObjectFactory.createOrderTable(1, true);
        OrderTable orderTableB = TestObjectFactory.createOrderTable(2, true);
        OrderTable savedOrderTableA = tableService.create(orderTableA);
        OrderTable savedOrderTableB = tableService.create(orderTableB);
        List<OrderTable> orderTables = Arrays.asList(savedOrderTableA, savedOrderTableB);

        TableGroup tableGroup = TestObjectFactory
            .createTableGroup(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertAll(() -> {
            assertThat(savedTableGroup).isInstanceOf(TableGroup.class);
            assertThat(savedTableGroup.getId()).isNotNull();
            assertThat(savedTableGroup.getCreatedDate()).isNotNull();
            assertThat(savedTableGroup.getOrderTables()).isNotNull();
            assertThat(savedTableGroup.getOrderTables()).isNotEmpty();
            assertThat(savedTableGroup.getOrderTables()).hasSize(2);
        });
    }

    @DisplayName("테이블 그룹을 생성한다. - 테이블이 비어있지 않을 경우")
    @Test
    void create_IfTableNotEmpty_ThrowException() {
        OrderTable orderTableA = TestObjectFactory.createOrderTable(1, false);
        OrderTable orderTableB = TestObjectFactory.createOrderTable(2, false);
        OrderTable savedOrderTableA = tableService.create(orderTableA);
        OrderTable savedOrderTableB = tableService.create(orderTableB);
        List<OrderTable> orderTables = Arrays.asList(savedOrderTableA, savedOrderTableB);

        TableGroup tableGroup = TestObjectFactory
            .createTableGroup(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 생성한다. - 테이블 갯수가 2 미만일 경우")
    @Test
    void create_IfTableIsLessThanTwo_ThrowException() {
        OrderTable orderTable = TestObjectFactory.createOrderTable(1, true);
        OrderTable savedOrderTable = tableService.create(orderTable);
        List<OrderTable> orderTables = Collections.singletonList(savedOrderTable);

        TableGroup tableGroup = TestObjectFactory
            .createTableGroup(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        OrderTable orderTableA = TestObjectFactory.createOrderTable(111, true);
        OrderTable orderTableB = TestObjectFactory.createOrderTable(222, true);
        OrderTable savedOrderTableA = tableService.create(orderTableA);
        OrderTable savedOrderTableB = tableService.create(orderTableB);
        List<OrderTable> orderTables = Arrays.asList(savedOrderTableA, savedOrderTableB);

        TableGroup tableGroup = TestObjectFactory
            .createTableGroup(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        tableGroupService.ungroup(savedTableGroup.getId());

        Optional<OrderTable> ungroupedOrderTableA = orderTableDao
            .findById(savedOrderTableA.getId());
        Optional<OrderTable> ungroupedOrderTableB = orderTableDao
            .findById(savedOrderTableB.getId());

        assertAll(() -> {
            assertThat(ungroupedOrderTableA).isNotEqualTo(Optional.empty());
            assertThat(ungroupedOrderTableA.get()
                .getTableGroupId()).isNull();
            assertThat(ungroupedOrderTableA.get()
                .isEmpty()).isFalse();
            assertThat(ungroupedOrderTableB).isNotEqualTo(Optional.empty());
            assertThat(ungroupedOrderTableB.get()
                .getTableGroupId()).isNull();
            assertThat(ungroupedOrderTableB.get()
                .isEmpty()).isFalse();
        });
    }

    @DisplayName("테이블 그룹을 해제한다. - 주문이 Cooking, Meal일 경우")
    @Test
    void ungroup_IfStatusIsCookingOrMeal_ThrowException() {
        OrderTable orderTableA = TestObjectFactory.createOrderTable(1, true);
        OrderTable orderTableB = TestObjectFactory.createOrderTable(2, true);
        OrderTable savedOrderTableA = tableService.create(orderTableA);
        OrderTable savedOrderTableB = tableService.create(orderTableB);
        List<OrderTable> orderTables = Arrays.asList(savedOrderTableA, savedOrderTableB);

        TableGroup tableGroup = TestObjectFactory
            .createTableGroup(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        List<OrderLineItem> orderLineItems
            = Collections.singletonList(TestObjectFactory.createOrderLineItem(1L, 1L));
        Order order
            = TestObjectFactory.createOrder(savedOrderTableA.getId(), null, orderLineItems);
        orderService.create(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
