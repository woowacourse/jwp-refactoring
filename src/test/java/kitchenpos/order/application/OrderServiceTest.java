package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.request.OrderCreationRequest;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.request.OrderStatusUpdateRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@Sql(value = "/initialization.sql")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuValidator menuValidator;

    @Autowired
    private OrderValidator orderValidator;

    @DisplayName("주문 하려는 메뉴가 존재하지 않을 경우, 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderLineItemIsNotExists() {
        //given
        Long invalidId = 99L;
        OrderTable orderTable = saveOrderTableForEmpty(false);

        List<OrderLineItemRequest> orderLineItemRequests = List.of(new OrderLineItemRequest(invalidId, 1L));
        OrderCreationRequest request = new OrderCreationRequest(orderTable.getId(), orderLineItemRequests);

        assertThat(menuRepository.findById(invalidId)).isEmpty();

        //when then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상품에 존재하지 않는 메뉴가 존재합니다.");
    }

    @DisplayName("주문을 한 테이블이 존재하지 않을 경우, 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderTableIsNotExists() {
        //given
        Long invalidId = 99L;
        Menu menu = saveMenu();

        List<OrderLineItemRequest> orderLineItemRequests = List.of(new OrderLineItemRequest(menu.getId(), 1L));
        OrderCreationRequest request = new OrderCreationRequest(invalidId, orderLineItemRequests);

        assertThat(menuRepository.findById(menu.getId())).isPresent();
        assertThat(orderTableRepository.findById(invalidId)).isEmpty();

        //when then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("ID에 해당하는 주문 테이블을 찾을 수 없습니다.");
    }

    @DisplayName("주문을 한 테이블이 주문할 수 없는(empty) 경우, 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderTableIsEmpty() {
        //given
        Menu menu = saveMenu();
        OrderTable orderTable = saveOrderTableForEmpty(true);

        List<OrderLineItemRequest> orderLineItemRequests = List.of(new OrderLineItemRequest(menu.getId(), 1L));
        OrderCreationRequest request = new OrderCreationRequest(orderTable.getId(), orderLineItemRequests);

        Optional<OrderTable> findOrderTable = orderTableRepository.findById(orderTable.getId());

        assertThat(menuRepository.findById(menu.getId())).isPresent();
        assertThat(findOrderTable).isPresent();
        assertThat(findOrderTable.get().isEmpty()).isTrue();

        //when then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문할 수 없는 상태의 테이블이 존재합니다.");
    }

    @DisplayName("주문이 생성될 때, 주문 관련 정보가 변경된다.")
    @Test
    void createSuccessTest_ChangeOrderInformation() {
        //given
        Menu menu = saveMenu();
        OrderTable orderTable = saveOrderTableForEmpty(false);

        List<OrderLineItemRequest> orderLineItemRequests = List.of(new OrderLineItemRequest(menu.getId(), 1L));
        OrderCreationRequest request = new OrderCreationRequest(orderTable.getId(), orderLineItemRequests);

        //when
        OrderResponse response = orderService.create(request);

        //then
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("주문이 생성될 때, 주문에 포함된 메뉴들도 저장된다.")
    @Test
    void createSuccessTest_SaveWithOrderLineItems() {
        //given
        Menu menu = saveMenu();
        OrderTable orderTable = saveOrderTableForEmpty(false);

        List<OrderLineItemRequest> orderLineItemRequests = List.of(new OrderLineItemRequest(menu.getId(), 1L));
        OrderCreationRequest request = new OrderCreationRequest(orderTable.getId(), orderLineItemRequests);

        Optional<OrderLineItem> findOrderLineItemBeforeCreatingOrder = orderLineItemRepository.findAll()
                .stream()
                .filter(item -> item.getMenuId().equals(menu.getId()))
                .findAny();

        assertThat(findOrderLineItemBeforeCreatingOrder).isEmpty();

        //when
        orderService.create(request);

        //then
        Optional<OrderLineItem> findOrderLineItemAfterCreatingOrder = orderLineItemRepository.findAll()
                .stream()
                .filter(item -> item.getMenuId().equals(menu.getId()))
                .findAny();

        assertThat(findOrderLineItemAfterCreatingOrder).isPresent();
    }

    @DisplayName("모든 주문 목록을 조회하면, 각 주문에 포함된 메뉴들도 함께 조회된다.")
    @Test
    void listSuccessTest() {
        //given
        Menu menu = saveMenu();
        OrderTable orderTable = saveOrderTableForEmpty(false);

        List<OrderLineItemRequest> orderLineItemRequests = List.of(new OrderLineItemRequest(menu.getId(), 1L));
        OrderCreationRequest request = new OrderCreationRequest(orderTable.getId(), orderLineItemRequests);
        OrderResponse response = orderService.create(request);

        //when
        Order savedOrder = orderRepository.findById(response.getId()).get();
        OrderLineItem expectedOrderLineItem = OrderLineItem.create(menu.getId(), 1L);

        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAll();

        //then
        assertThat(orderLineItems).hasSize(1);
        assertThat(orderLineItems.get(0))
                .usingRecursiveComparison()
                .ignoringFields("seq")
                .isEqualTo(expectedOrderLineItem);
    }

    @DisplayName("변경하려는 주문이 존재하지 않으면 변경할 수 없다.")
    @Test
    void changeOrderStatusFailTest_ByOrderIsNotExists() {
        //given
        Long invalidId = 99L;
        assertThat(orderRepository.findById(invalidId)).isEmpty();

        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest("COMPLETION");

        //when then
        assertThatThrownBy(() -> orderService.changeOrderStatus(invalidId, request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("ID에 해당하는 주문이 존재하지 않습니다.");
    }

    @DisplayName("주문이 완료(COMPLETION) 상태인 경우, 변경할 수 없다.")
    @Test
    void changeOrderStatusFailTest_ByOrderStatusIsCompletion() {
        //given
        Menu menu = saveMenu();
        OrderTable savedOrderTable = saveOrderTableForEmpty(false);
        OrderLineItems orderLineItems = OrderLineItems.from(List.of(createOrderLineItem(menu)));
        Order order = Order.create(savedOrderTable.getId(), orderLineItems, orderValidator);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        Long savedOrderId = orderRepository.save(order).getId();
        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest("COMPLETION");

        //when then
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrderId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Completion 상태일 경우, 주문 상태를 변경할 수 없습니다.");
    }

    @DisplayName("주문의 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatusSuccessTest() {
        //given
        Menu menu = saveMenu();
        OrderTable savedOrderTable = saveOrderTableForEmpty(false);
        OrderLineItems orderLineItems = OrderLineItems.from(List.of(createOrderLineItem(menu)));
        Order order = Order.create(savedOrderTable.getId(), orderLineItems, orderValidator);

        Long savedOrderId = orderRepository.save(order).getId();

        //when
        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest("COMPLETION");
        orderService.changeOrderStatus(savedOrderId, request);

        //then
        Order findOrder = orderRepository.findById(savedOrderId).get();

        assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    private Menu saveMenu() {
        MenuGroup savedMenuGroup = saveMenuGroup();
        Product product = saveProduct();
        MenuProducts menuProducts = MenuProducts.from(List.of(createMenuProduct(product)));

        Menu menu = Menu.create("TestMenu", BigDecimal.TEN, savedMenuGroup, menuProducts, menuValidator);

        return menuRepository.save(menu);
    }

    private MenuGroup saveMenuGroup() {
        MenuGroup menuGroup = MenuGroup.from("TestMenuGroup");

        return menuGroupRepository.save(menuGroup);
    }

    private Product saveProduct() {
        Product product = Product.create("TestProduct", BigDecimal.TEN);

        return productRepository.save(product);
    }

    private MenuProduct createMenuProduct(Product product) {
        return MenuProduct.create(1L, product.getId());
    }

    private OrderTable saveOrderTableForEmpty(boolean empty) {
        OrderTable orderTable = OrderTable.createWithoutTableGroup(0, empty);

        return orderTableRepository.save(orderTable);
    }

    private OrderLineItem createOrderLineItem(Menu menu) {
        return OrderLineItem.create(menu.getId(), 1L);
    }

}
