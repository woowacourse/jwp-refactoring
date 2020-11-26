package kitchenpos.table_group.application;

import static kitchenpos.TestObjectFactory.createMenu;
import static kitchenpos.TestObjectFactory.createMenuProduct;
import static kitchenpos.TestObjectFactory.createOrder;
import static kitchenpos.TestObjectFactory.createOrderLineItem;
import static kitchenpos.TestObjectFactory.createOrderTable;
import static kitchenpos.TestObjectFactory.createOrderTableIdRequest;
import static kitchenpos.TestObjectFactory.createProduct;
import static kitchenpos.TestObjectFactory.createTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.dto.OrderTableIdRequest;
import kitchenpos.order_table.repository.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.table_group.dto.TableGroupRequest;
import kitchenpos.table_group.dto.TableGroupResponse;
import kitchenpos.table_group.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Sql(value = "/deleteAll.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("테이블 그룹 추가")
    @Test
    void create() {
        OrderTable savedTable1 = orderTableRepository.save(createOrderTable(true));
        OrderTable savedTable2 = orderTableRepository.save(createOrderTable(true));
        OrderTableIdRequest tableIdRequest1 = createOrderTableIdRequest(savedTable1.getId());
        OrderTableIdRequest tableIdRequest2 = createOrderTableIdRequest(savedTable2.getId());
        List<OrderTableIdRequest> tables = Arrays.asList(tableIdRequest1, tableIdRequest2);

        TableGroupRequest request = createTableGroupRequest(tables);
        TableGroupResponse savedTableGroup = tableGroupService.create(request);

        List<OrderTable> savedTables = orderTableRepository
            .findAllByTableGroupId(savedTableGroup.getId());

        assertAll(
            () -> assertThat(savedTableGroup.getId()).isNotNull(),
            () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull(),
            () -> assertThat(savedTables.get(0).isEmpty()).isFalse(),
            () -> assertThat(savedTables.get(1).isEmpty()).isFalse()
        );
    }

    @DisplayName("[예외] 2개 미만의 테이블을 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_NoTable() {
        OrderTable savedTable1 = orderTableRepository.save(createOrderTable(true));
        OrderTableIdRequest tableIdRequest1 = createOrderTableIdRequest(savedTable1.getId());
        List<OrderTableIdRequest> tables = Arrays.asList(tableIdRequest1);

        TableGroupRequest request = createTableGroupRequest(tables);

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 저장되지 않은 포함한 테이블 그룹 추가")
    @Test
    void create_Fail_With_NotExistTable() {
        OrderTable savedTable = orderTableRepository.save(createOrderTable(true));
        OrderTable notSavedTable = OrderTable.builder().build();
        OrderTableIdRequest savedTableIdRequest = createOrderTableIdRequest(savedTable.getId());
        OrderTableIdRequest notSavedTableIdRequest = createOrderTableIdRequest(
            notSavedTable.getId());
        List<OrderTableIdRequest> tables = Arrays
            .asList(savedTableIdRequest, notSavedTableIdRequest);

        TableGroupRequest request = createTableGroupRequest(tables);

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() {
        OrderTable savedTable1 = orderTableRepository.save(createOrderTable(true));
        OrderTable savedTable2 = orderTableRepository.save(createOrderTable(true));
        OrderTableIdRequest tableIdRequest1 = createOrderTableIdRequest(savedTable1.getId());
        OrderTableIdRequest tableIdRequest2 = createOrderTableIdRequest(savedTable2.getId());
        List<OrderTableIdRequest> tables = Arrays.asList(tableIdRequest1, tableIdRequest2);

        TableGroupRequest tableGroup = createTableGroupRequest(tables);
        TableGroupResponse savedTableGroup = tableGroupService.create(tableGroup);

        tableGroupService.ungroup(savedTableGroup.getId());

        OrderTable ungroupedTable1 = orderTableRepository.findById(savedTable1.getId()).get();
        OrderTable ungroupedTable2 = orderTableRepository.findById(savedTable2.getId()).get();
        assertAll(
            () -> assertThat(ungroupedTable1.getTableGroup()).isNull(),
            () -> assertThat(ungroupedTable2.getTableGroup()).isNull(),
            () -> assertThat(ungroupedTable1.isEmpty()).isFalse(),
            () -> assertThat(ungroupedTable2.isEmpty()).isFalse()
        );
    }

    @Transactional
    @DisplayName("[예외] 조리, 식사 중인 테이블을 포함한 테이블 그룹 해제")
    @Test
    void ungroup_Fail_With_TableInProgress() {
        OrderTable savedTable1 = orderTableRepository.save(createOrderTable(true));
        OrderTable savedTable2 = orderTableRepository.save(createOrderTable(true));
        OrderTableIdRequest tableIdRequest1 = createOrderTableIdRequest(savedTable1.getId());
        OrderTableIdRequest tableIdRequest2 = createOrderTableIdRequest(savedTable2.getId());
        List<OrderTableIdRequest> tables = Arrays.asList(tableIdRequest1, tableIdRequest2);

        TableGroupRequest tableGroup = createTableGroupRequest(tables);
        TableGroupResponse savedTableGroup = tableGroupService.create(tableGroup);

        OrderTable groupedTable = tableGroupRepository.findById(savedTableGroup.getId())
            .get()
            .getOrderTables()
            .get(0);

        Product product = createProduct(10_000);
        MenuProduct menuProduct = createMenuProduct(product, 2);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct);
        Menu menu = createMenu(menuProducts, 18_000);
        OrderLineItem orderLineItem = createOrderLineItem(menu);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);
        Order order = createOrder(groupedTable, orderLineItems);
        orderRepository.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}