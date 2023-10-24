package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.order.dto.request.TableGroupCreationRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
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

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuValidator menuValidator;

    @Autowired
    private OrderValidator orderValidator;

    @DisplayName("주문 테이블이 2개 미만이면, 주문 테이블 그룹을 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderTableCountIsLessThanTwo() {
        //given
        OrderTable savedOrderTable = saveOrderTableForEmpty(false);

        List<OrderTableRequest> orderTableRequests = List.of(new OrderTableRequest(savedOrderTable.getId()));
        TableGroupCreationRequest request = new TableGroupCreationRequest(orderTableRequests);

        //when then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹화 할 테이블 개수는 2 이상이어야 합니다");
    }

    @DisplayName("주문 테이블이 존재하지 않으면, 주문 테이블 그룹을 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderTableIsNotExists() {
        //given
        Long invalidId1 = 99L;
        Long invalidId2 = 100L;

        List<OrderTableRequest> orderTableRequests = List.of(
                new OrderTableRequest(invalidId1),
                new OrderTableRequest(invalidId2)
        );
        TableGroupCreationRequest request = new TableGroupCreationRequest(orderTableRequests);

        assertThat(orderTableRepository.findById(invalidId1)).isEmpty();
        assertThat(orderTableRepository.findById(invalidId2)).isEmpty();

        //when then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 테이블이 존재합니다.");
    }

    @DisplayName("주문이 가능한 테이블이 존재하면(Not Empty), 주문 테이블 그룹을 생성할 수 없다.")
    @Test
    void createFailTest_ByTableGroupContainsNotEmptyOrderTable() {
        //given
        OrderTable savedOrderTable1 = saveOrderTableForEmpty(false);
        OrderTable savedOrderTable2 = saveOrderTableForEmpty(true);

        //when then
        List<OrderTableRequest> orderTableRequests = List.of(
                new OrderTableRequest(savedOrderTable1.getId()),
                new OrderTableRequest(savedOrderTable2.getId())
        );
        TableGroupCreationRequest request = new TableGroupCreationRequest(orderTableRequests);

        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 가능한 상태의 테이블이 존재합니다.");
    }

    @DisplayName("다른 테이블 그룹에 속해있는 주문 테이블이 있는 경우, 주문 테이블 그룹을 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderTableIsAlreadyGrouped() {
        //given
        OrderTable savedOrderTable1 = saveOrderTableForEmpty(true);
        OrderTable savedOrderTable2 = saveOrderTableForEmpty(true);
        TableGroup tableGroup = TableGroup.createWithGrouping(List.of(savedOrderTable1, savedOrderTable2));

        tableGroupRepository.save(tableGroup);

        OrderTable newOrderTable = saveOrderTableForEmpty(true);

        //when then
        List<OrderTableRequest> newOrderTableRequests = List.of(
                new OrderTableRequest(savedOrderTable1.getId()),
                new OrderTableRequest(newOrderTable.getId())
        );
        TableGroupCreationRequest newRequest = new TableGroupCreationRequest(newOrderTableRequests);

        assertThatThrownBy(() -> tableGroupService.create(newRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 다른 그룹에 속해있는 테이블이 존재합니다.");
    }

    @DisplayName("주문 테이블 그룹을 생성하면, 생성 시점을 초기화 하고 주문 테이블은 해당 그룹에 속하며 각 주문 테이블은 주문 가능한 상태로 변한다.")
    @Test
    void createSuccessTest_InitializeRelatedData() {
        //given
        OrderTable savedOrderTable1 = saveOrderTableForEmpty(true);
        OrderTable savedOrderTable2 = saveOrderTableForEmpty(true);

        List<OrderTableRequest> orderTableRequests = List.of(
                new OrderTableRequest(savedOrderTable1.getId()),
                new OrderTableRequest(savedOrderTable2.getId())
        );
        TableGroupCreationRequest request = new TableGroupCreationRequest(orderTableRequests);

        //when
        Long savedTableGroupId = tableGroupService.create(request).getId();

        //then
        TableGroup findTableGroup = tableGroupRepository.findById(savedTableGroupId).get();
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

        Menu menu = saveMenu();
        OrderLineItems orderLineItems = OrderLineItems.from(List.of(createOrderLineItem(menu)));
        Order order = Order.create(savedOrderTable1.getId(), orderLineItems, orderValidator);
        order.changeOrderStatus(status);

        orderRepository.save(order);

        savedOrderTable1.changeEmpty(true);
        savedOrderTable2.changeEmpty(true);

        List<OrderTableRequest> orderTableRequests = List.of(
                new OrderTableRequest(savedOrderTable1.getId()),
                new OrderTableRequest(savedOrderTable2.getId())
        );
        TableGroupCreationRequest request = new TableGroupCreationRequest(orderTableRequests);

        Long savedTableGroupId = tableGroupService.create(request).getId();

        //when then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문이 완료되지 않은 상태의 테이블이 존재합니다.");
    }

    @DisplayName("그룹을 해제하면, 각 주문 테이블은 해당 그룹에 속하지 않고, 주문이 가능한 상태(Not Empty)로 변한다.")
    @Test
    void ungroupSuccessTest_InitializeRelatedData() {
        OrderTable savedOrderTable1 = saveOrderTableForEmpty(false);
        OrderTable savedOrderTable2 = saveOrderTableForEmpty(false);

        Menu menu = saveMenu();
        OrderLineItems orderLineItems = OrderLineItems.from(List.of(createOrderLineItem(menu)));
        Order order = Order.create(savedOrderTable1.getId(), orderLineItems, orderValidator);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        savedOrderTable1.changeEmpty(true);
        savedOrderTable2.changeEmpty(true);

        orderRepository.save(order);

        List<OrderTableRequest> orderTableRequests = List.of(
                new OrderTableRequest(savedOrderTable1.getId()),
                new OrderTableRequest(savedOrderTable2.getId())
        );
        TableGroupCreationRequest request = new TableGroupCreationRequest(orderTableRequests);

        Long savedTableGroupId = tableGroupService.create(request).getId();

        //when
        tableGroupService.ungroup(savedTableGroupId);

        //then
        assertThat(orderTableRepository.findAllByTableGroupId(savedTableGroupId)).isEmpty();
        assertThat(orderTableRepository.findAllByIdIn(List.of(savedOrderTable1.getId(), savedOrderTable2.getId())))
                .extractingResultOf("isEmpty")
                .containsExactly(false, false);
    }

    private OrderTable saveOrderTableForEmpty(boolean empty) {
        OrderTable orderTable = OrderTable.createWithoutTableGroup(0, empty);

        return orderTableRepository.save(orderTable);
    }

    private Menu saveMenu() {
        MenuGroup menuGroup = saveMenuGroup();
        Product product = saveProduct();
        MenuProducts menuProducts = MenuProducts.from(List.of(createMenuProduct(product)));
        Menu menu = Menu.create("TestMenu", BigDecimal.TEN, menuGroup, menuProducts, menuValidator);

        return menuRepository.save(menu);
    }

    private MenuGroup saveMenuGroup() {
        MenuGroup menuGroup = MenuGroup.from("TestMenuGroup");

        return menuGroupRepository.save(menuGroup);
    }

    private MenuProduct createMenuProduct(Product product) {
        return MenuProduct.create(1L, product.getId());
    }

    private Product saveProduct() {
        Product product = Product.create("TestProduct", BigDecimal.TEN);

        return productRepository.save(product);
    }

    private OrderLineItem createOrderLineItem(Menu menu) {
        return OrderLineItem.create(menu.getId(), 1L);
    }

}
