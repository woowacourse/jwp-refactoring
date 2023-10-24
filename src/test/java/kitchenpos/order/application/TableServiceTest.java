package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.table.dto.request.TableEmptyUpdateRequest;
import kitchenpos.table.dto.request.TableNumberOfGuestsUpdateRequest;
import kitchenpos.table.dto.response.TableResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@Sql(value = "/initialization.sql")
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuValidator menuValidator;

    @Autowired
    private OrderValidator orderValidator;

    @DisplayName("기존에 주문이 없었던 테이블인 경우, 주문 상태를 변경할 수 없다.")
    @Test
    void changeEmptyFailTest_ByOrderTableIsNotExists() {
        //given
        Long invalidId = 99L;
        TableEmptyUpdateRequest request = new TableEmptyUpdateRequest(Boolean.FALSE);

        assertThat(orderTableRepository.findById(invalidId)).isEmpty();

        //when then
        assertThatThrownBy(() -> tableService.changeEmpty(invalidId, request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("ID에 해당하는 주문 테이블이 존재하지 않습니다.");
    }

    @DisplayName("그룹화 되어있는 테이블이 존재하는 경우, 주문 상태를 변경할 수 없다.")
    @Test
    void changeEmptyFailTest_ByAlreadyGroupedOtherTable() {
        //given
        OrderTable orderTable1 = saveOrderTableForEmpty(true);
        OrderTable orderTable2 = saveOrderTableForEmpty(true);

        TableGroup tableGroup = TableGroup.createWithGrouping(List.of(orderTable1, orderTable2));
        tableGroupRepository.save(tableGroup);

        //when then
        TableEmptyUpdateRequest request = new TableEmptyUpdateRequest(Boolean.FALSE);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("다른 그룹에 속해있으므로, 주문 상태를 변경할 수 없습니다.");
    }


    @ParameterizedTest(name = "완료(COMPLETION)되지 않은 상태(COOKING, MEAL)의 주문이 있는 경우, 변경할 수 없다.")
    @EnumSource(mode = Mode.INCLUDE, names = {"COOKING", "MEAL"})
    void changeEmptyFailTest_ByOrderStatusIsNotCompletion(OrderStatus orderStatus) {
        //given
        OrderTable orderTable = saveOrderTableForEmpty(false);
        Menu menu = saveMenu();
        OrderLineItems orderLineItems = OrderLineItems.from(List.of(createOrderLineItem(menu)));
        Order order = Order.create(orderTable.getId(), orderLineItems, orderValidator);
        order.changeOrderStatus(orderStatus);

        orderRepository.save(order);

        assertThat(order.getOrderStatus()).isNotEqualTo(OrderStatus.COMPLETION);

        //when then
        TableEmptyUpdateRequest request = new TableEmptyUpdateRequest(Boolean.FALSE);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Completion 상태가 아닌 주문 테이블은 주문 가능 여부를 변경할 수 없습니다.");
    }

    @DisplayName("테이블의 주문 가능 상태(Empty)를 변경할 수 있다.")
    @Test
    void changeEmptySuccessTest() {
        //given
        OrderTable orderTable = saveOrderTableForEmpty(false);
        Menu menu = saveMenu();
        OrderLineItems orderLineItems = OrderLineItems.from(List.of(createOrderLineItem(menu)));
        Order order = Order.create(orderTable.getId(), orderLineItems, orderValidator);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        orderRepository.save(order);

        OrderTable findOrderTable = orderTableRepository.findById(orderTable.getId()).get();
        assertThat(findOrderTable.isEmpty()).isFalse();

        //when
        TableEmptyUpdateRequest request = new TableEmptyUpdateRequest(Boolean.TRUE);

        TableResponse response = tableService.changeEmpty(orderTable.getId(), request);

        //then
        assertThat(response.isEmpty()).isTrue();
    }

    @ParameterizedTest(name = "방문한 손님 수가 0명 미만이면, 테이블에 방문한 손님 수를 변경할 수 없다.")
    @ValueSource(ints = {-100, -1})
    void changeNumberOfGuestsFailTest_ByNumberOfGuestsIsLessThanZero(int numberOfGuests) {
        //given
        OrderTable orderTable = saveOrderTableForEmpty(false);
        Menu menu = saveMenu();
        OrderLineItems orderLineItems = OrderLineItems.from(List.of(createOrderLineItem(menu)));
        Order order = Order.create(orderTable.getId(), orderLineItems, orderValidator);

        orderRepository.save(order);

        //when //then
        TableNumberOfGuestsUpdateRequest request = new TableNumberOfGuestsUpdateRequest(numberOfGuests);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블에 방문한 손님 수는 0 이상이어야 합니다.");
    }

    @DisplayName("변경하려고 하는 테이블이 존재하지 않으면, 테이블에 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsFailTest_ByOrderTableIsNotExists() {
        //given
        Long invalidId = 99L;
        OrderTable orderTable = saveOrderTableForEmpty(false);
        Menu menu = saveMenu();
        OrderLineItems orderLineItems = OrderLineItems.from(List.of(createOrderLineItem(menu)));
        Order order = Order.create(orderTable.getId(), orderLineItems, orderValidator);

        orderRepository.save(order);

        assertThat(orderTableRepository.findById(invalidId)).isEmpty();

        //when then
        TableNumberOfGuestsUpdateRequest request = new TableNumberOfGuestsUpdateRequest(10);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(invalidId, request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("주문할 수 없는 테이블(Empty)이면, 테이블에 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsFailTest_ByOrderTableIsEmpty() {
        //given
        OrderTable orderTable = saveOrderTableForEmpty(false);
        Menu menu = saveMenu();
        OrderLineItems orderLineItems = OrderLineItems.from(List.of(createOrderLineItem(menu)));
        Order order = Order.create(orderTable.getId(), orderLineItems, orderValidator);

        orderRepository.save(order);

        orderTable.changeEmpty(true);

        //when then
        TableNumberOfGuestsUpdateRequest request = new TableNumberOfGuestsUpdateRequest(10);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문을 할 수 없는 상태이므로, 방문 손님 수를 변경할 수 없습니다.");
    }

    @ParameterizedTest(name = "테이블에 방문한 손님 수를 변경할 수 있다.")
    @ValueSource(ints = {1, 1000})
    void changeNumberOfGuestsSuccessTest(int numberOfGuests) {
        //given
        OrderTable orderTable = saveOrderTableForEmpty(false);
        Menu menu = saveMenu();
        OrderLineItems orderLineItems = OrderLineItems.from(List.of(createOrderLineItem(menu)));
        Order order = Order.create(orderTable.getId(), orderLineItems, orderValidator);
        orderRepository.save(order);

        assertThat(orderTable.getNumberOfGuests()).isZero();

        //when
        TableNumberOfGuestsUpdateRequest request = new TableNumberOfGuestsUpdateRequest(numberOfGuests);

        tableService.changeNumberOfGuests(orderTable.getId(), request);

        //then
        OrderTable findOrderTable = orderTableRepository.findById(orderTable.getId()).get();

        assertThat(findOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @DisplayName("테이블 목록을 조회할 수 있다.")
    @Test
    void listSuccessTest() {
        //given
        OrderTable orderTable = OrderTable.createWithoutTableGroup(10, Boolean.TRUE);

        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        //when
        List<TableResponse> responses = tableService.list();
        TableResponse expected = TableResponse.from(savedOrderTable);
        //then
        assertThat(responses).usingRecursiveComparison()
                .isEqualTo(List.of(expected));
    }

    private OrderTable saveOrderTableForEmpty(boolean empty) {
        OrderTable orderTable = OrderTable.createWithoutTableGroup(0, empty);

        return orderTableRepository.save(orderTable);
    }

    private Menu saveMenu() {
        MenuGroup menuGroup = saveMenuGroup();
        Product product = saveProduct();
        MenuProducts menuProducts = MenuProducts.from(List.of(createMenuProduct(product)));
        Menu menu = Menu.create("TestMenu", BigDecimal.TEN, menuGroup.getId(), menuProducts, menuValidator);

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
