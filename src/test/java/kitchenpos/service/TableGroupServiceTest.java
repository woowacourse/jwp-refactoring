package kitchenpos.service;

import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.utils.TestObjectUtils.NOT_EXIST_VALUE;
import static kitchenpos.utils.TestObjectUtils.createOrder;
import static kitchenpos.utils.TestObjectUtils.createOrderLineItem;
import static kitchenpos.utils.TestObjectUtils.createOrderTable;
import static kitchenpos.utils.TestObjectUtils.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    private OrderTable emptyOrderTable;
    private OrderTable emptyOrderTable2;
    private OrderTable notEmptyOrderTable;

    @BeforeEach
    void setUp() {
        emptyOrderTable = tableService.create(createOrderTable(0, true));
        emptyOrderTable2 = tableService.create(createOrderTable(0, true));
        notEmptyOrderTable = tableService.create(createOrderTable(4, false));
    }

    @DisplayName("테이블 그룹 생성 - 성공")
    @Test
    void create_SuccessToCreate() {
        List<OrderTable> orderTables = Arrays.asList(emptyOrderTable, emptyOrderTable2);
        TableGroup tableGroup = createTableGroup(orderTables);

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertAll(() -> {
            assertThat(savedTableGroup.getId()).isNotNull();
            assertThat(savedTableGroup.getCreatedDate()).isNotNull();
            assertThat(savedTableGroup.getOrderTables()).hasSize(orderTables.size());
        });
    }

    @DisplayName("테이블 그룹 생성 - 예외, 주문 테이블이 빈 경우")
    @Test
    void create_OrderTablesIsEmpty_ThrownException() {
        List<OrderTable> orderTables = Collections.emptyList();
        TableGroup tableGroup = createTableGroup(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 생성 - 예외, 주문 테이블 개수가 2보다 작은 경우")
    @Test
    void create_OrderTablesLessThanTwo_ThrownException() {
        List<OrderTable> orderTables = Collections.singletonList(emptyOrderTable);
        TableGroup tableGroup = createTableGroup(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 생성 - 예외, 주문 테이블을 찾을 수 없는 경우")
    @Test
    void create_NotFoundOrderTable_ThrownException() {
        emptyOrderTable.setId(NOT_EXIST_VALUE);
        List<OrderTable> orderTables = Arrays.asList(emptyOrderTable, emptyOrderTable2);
        TableGroup tableGroup = createTableGroup(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 생성 - 예외, 주문 테이블이 빈 테이블이 아닌 경우")
    @Test
    void create_OrderTableIsNotEmpty_ThrownException() {
        List<OrderTable> orderTables = Arrays.asList(emptyOrderTable, notEmptyOrderTable);
        TableGroup tableGroup = createTableGroup(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 생성 - 예외, 주문 테이블에 테이블 그룹이 이미 있는 경우")
    @Test
    void create_OrderTableAlreadyExistTableGroup_ThrownException() {
        List<OrderTable> orderTables = Arrays.asList(emptyOrderTable, emptyOrderTable2);
        TableGroup tableGroup = createTableGroup(orderTables);
        tableGroupService.create(tableGroup);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 해제 - 성공")
    @Test
    void ungroup_SuccessToUngroup() {
        List<OrderTable> orderTables = Arrays.asList(emptyOrderTable, emptyOrderTable2);
        TableGroup tableGroup = createTableGroup(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        tableGroupService.ungroup(savedTableGroup.getId());
        assertThatThrownBy(() -> tableGroupService.create(savedTableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 해제 - 예외, 주문 테이블의 주문 상태가 COOKING인 경우")
    @Test
    void ungroup_OrderTableStatusIsCooking_ThrownException() {
        List<OrderTable> orderTables = Arrays.asList(emptyOrderTable, emptyOrderTable2);
        TableGroup tableGroup = createTableGroup(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        List<OrderLineItem> orderLineItems = Collections.singletonList(createOrderLineItem(1L, 1L));
        Order order = createOrder(emptyOrderTable.getId(), orderLineItems);
        orderService.create(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 해제 - 예외, 주문 테이블의 주문 상태가 MEAL인 경우")
    @Test
    void ungroup_OrderTableStatusIsMeal_ThrownException() {
        List<OrderTable> orderTables = Arrays.asList(emptyOrderTable, emptyOrderTable2);
        TableGroup tableGroup = createTableGroup(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        List<OrderLineItem> orderLineItems = Collections.singletonList(createOrderLineItem(1L, 1L));
        Order order = createOrder(emptyOrderTable.getId(), orderLineItems);
        Order savedOrder = orderService.create(order);
        savedOrder.setOrderStatus(MEAL.name());
        orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
