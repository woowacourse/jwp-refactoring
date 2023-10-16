package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@Sql(value = "/initialization.sql")
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("주문 테이블이 2개 미만이면, 주문 테이블 그룹을 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderTableCountIsLessThanTwo() {
        //given
        OrderTable savedOrderTable = saveOrderTableForEmpty(false);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTable));

        //when then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않으면, 주문 테이블 그룹을 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderTableIsNotExists() {
        //given
        Long invalidId1 = 99L;
        Long invalidId2 = 100L;

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(invalidId1);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(invalidId2);

        assertThat(orderTableRepository.findById(invalidId1)).isEmpty();
        assertThat(orderTableRepository.findById(invalidId2)).isEmpty();

        //when then
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 가능한 테이블이 존재하면(Not Empty), 주문 테이블 그룹을 생성할 수 없다.")
    @Test
    void createFailTest_ByTableGroupContainsNotEmptyOrderTable() {
        //given
        OrderTable savedOrderTable1 = saveOrderTableForEmpty(false);
        OrderTable savedOrderTable2 = saveOrderTableForEmpty(true);

        //when then
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("다른 테이블 그룹에 속해있는 주문 테이블이 있는 경우, 주문 테이블 그룹을 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderTableIsAlreadyGrouped() {
        //given
        OrderTable savedOrderTable1 = saveOrderTableForEmpty(true);
        OrderTable savedOrderTable2 = saveOrderTableForEmpty(true);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

        tableGroupService.create(tableGroup);

        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setEmpty(true);

        OrderTable savedNewOrderTable = orderTableRepository.save(newOrderTable);

        //when then
        TableGroup newTableGroup = new TableGroup();
        newTableGroup.setOrderTables(List.of(savedOrderTable1, savedNewOrderTable));

        assertThatThrownBy(() -> tableGroupService.create(newTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 그룹을 생성하면, 생성 시점을 초기화 하고 주문 테이블은 해당 그룹에 속하며 각 주문 테이블은 주문 가능한 상태로 변한다.")
    @Test
    void createSuccessTest_InitializeRelatedData() {
        //given
        OrderTable savedOrderTable1 = saveOrderTableForEmpty(true);
        OrderTable savedOrderTable2 = saveOrderTableForEmpty(true);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

        //when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        //then
        TableGroup findTableGroup = tableGroupRepository.findById(savedTableGroup.getId()).get();
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(findTableGroup.getId());

        assertAll(
                () -> assertThat(findTableGroup.getId()).isNotNull(),
                () -> assertThat(findTableGroup.getCreatedDate()).isNotNull(),
                () -> assertThat(orderTables)
                        .extractingResultOf("getTableGroup")
                        .extractingResultOf("getId")
                        .containsExactly(findTableGroup.getId(), findTableGroup.getId()),
                () -> assertThat(orderTables)
                        .extractingResultOf("isEmpty")
                        .containsExactly(false, false)
        );
    }

    @ParameterizedTest(name = "완료(COMPLETION)되지 않은 상태의 테이블이 존재할 경우, 삭제할 수 없다.")
    @EnumSource(mode = Mode.INCLUDE, names = {"COOKING", "MEAL"})
    void ungroupFailTest_ByTableOrderStatusIsNotCompletion(OrderStatus status) {
        OrderTable savedOrderTable1 = saveOrderTableForEmpty(false);
        OrderTable savedOrderTable2 = saveOrderTableForEmpty(false);
//        Menu menu = saveMenu();

        OrderLineItem orderLineItem = new OrderLineItem();
//        orderLineItem.setMenu(menu);
//        orderLineItem.setQuantity(1L);
        Order order = Order.of(status, savedOrderTable1, List.of(orderLineItem));
//        orderLineItem.setOrder(order);

        orderRepository.save(order);

        savedOrderTable1.setEmpty(true);
        savedOrderTable2.setEmpty(true);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

        Long savedTableGroupId = tableGroupService.create(tableGroup).getId();

        //when then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹을 해제하면, 각 주문 테이블은 해당 그룹에 속하지 않고, 주문이 가능한 상태(Not Empty)로 변한다.")
    @Test
    void ungroupSuccessTest_InitializeRelatedData() {
        OrderTable savedOrderTable1 = saveOrderTableForEmpty(false);
        OrderTable savedOrderTable2 = saveOrderTableForEmpty(false);

        OrderLineItem orderLineItem = new OrderLineItem();

        Order order = Order.of(OrderStatus.COMPLETION, savedOrderTable1,List.of(orderLineItem));

        savedOrderTable1.setEmpty(true);
        savedOrderTable2.setEmpty(true);

        orderRepository.save(order);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

        Long savedTableGroupId = tableGroupService.create(tableGroup).getId();

        //when
        tableGroupService.ungroup(savedTableGroupId);

        //then
        assertThat(orderTableRepository.findAllByTableGroupId(savedTableGroupId)).isEmpty();
        assertThat(orderTableRepository.findAllByIdIn(List.of(savedOrderTable1.getId(), savedOrderTable2.getId())))
                .extractingResultOf("isEmpty")
                .containsExactly(false, false);
    }

    private OrderTable saveOrderTableForEmpty(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);

        return orderTableRepository.save(orderTable);
    }

    private Menu saveMenu() {
        Menu menu = Menu.of("TestMenu", BigDecimal.TEN, MenuGroup.from("TestMenuGroup"));

        return menuRepository.save(menu);
    }

    private OrderLineItem createOrderLineItem(Menu menu, Order order) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenu(menu);
        orderLineItem.setOrder(order);

        return orderLineItem;
    }

}
