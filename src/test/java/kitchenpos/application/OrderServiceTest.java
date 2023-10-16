package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
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

    private Order order;

    @BeforeEach
    void setup() {
        order = new Order();
    }

    @DisplayName("주문 메뉴(내역)가 비어있을 경우, 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderLineItemIsEmpty() {
        //given
        order.setOrderLineItems(Collections.emptyList());

        //when then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 하려는 메뉴가 존재하지 않을 경우, 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderLineItemIsNotExists() {
        //given
        Long invalidId = 99L;

        Menu menu = Menu.of("TestMenu", BigDecimal.TEN, MenuGroup.from("TestMenuGroup"));

        OrderLineItem orderLineItem = createOrderLineItem(menu);
        order.setOrderLineItems(List.of(orderLineItem));

        assertThat(menuRepository.findById(invalidId)).isEmpty();

        //when then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 한 테이블이 존재하지 않을 경우, 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderTableIsNotExists() {
        //given
        Long invalidId = 99L;

        Menu menu = saveMenu();
        OrderLineItem orderLineItem = createOrderLineItem(menu);

        OrderTable orderTable = new OrderTable();
        orderTable.setId(invalidId);

        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTable(orderTable);

        assertThat(menuRepository.findById(menu.getId())).isPresent();
        assertThat(orderTableRepository.findById(invalidId)).isEmpty();

        //when then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 한 테이블이 주문할 수 없는(empty) 경우, 생성할 수 없다.")
    @Test
    void createFailTest_ByOrderTableIsEmpty() {
        //given
        Menu menu = saveMenu();
        OrderTable savedOrderTable = saveOrderTableForEmpty(true);

        OrderLineItem orderLineItem = createOrderLineItem(menu);

        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTable(savedOrderTable);

        assertThat(menuRepository.findById(menu.getId())).isPresent();
        assertThat(orderTableRepository.findById(savedOrderTable.getId())).isPresent();
        assertThat(orderTableRepository.findById(savedOrderTable.getId()).get().isEmpty()).isTrue();

        //when then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 생성될 때, 주문 관련 정보가 변경된다.")
    @Test
    void createSuccessTest_ChangeOrderInformation() {
        //given
        Menu menu = saveMenu();
        OrderTable savedOrderTable = saveOrderTableForEmpty(false);

        OrderLineItem orderLineItem = createOrderLineItem(menu);

        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTable(savedOrderTable);

        assertThat(order.getOrderStatus()).isNull();
        assertThat(order.getOrderedTime()).isNull();

        //when
        Order savedOrder = orderService.create(order);

        //then
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(order.getOrderedTime()).isNotNull();
    }

    @DisplayName("주문이 생성될 때, 주문에 포함된 메뉴들도 저장된다.")
    @Test
    void createSuccessTest_SaveWithOrderLineItems() {
        //given
        Menu menu = saveMenu();
        OrderTable savedOrderTable = saveOrderTableForEmpty(false);

        OrderLineItem orderLineItem = createOrderLineItem(menu);

        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTable(savedOrderTable);

        Optional<OrderLineItem> findOrderLineItemBeforeCreatingOrder = orderLineItemRepository.findAll()
                .stream()
                .filter(item -> item.getMenu().getId().equals(menu.getId()))
                .findAny();

        assertThat(findOrderLineItemBeforeCreatingOrder).isEmpty();

        //when
        orderService.create(order);

        //then
        Optional<OrderLineItem> findOrderLineItemAfterCreatingOrder = orderLineItemRepository.findAll()
                .stream()
                .filter(item -> item.getMenu().getId().equals(menu.getId()))
                .findAny();

        assertThat(findOrderLineItemAfterCreatingOrder).isPresent();
    }

    @DisplayName("모든 주문 목록을 조회하면, 각 주문에 포함된 메뉴들도 함께 조회된다.")
    @Test
    void listSuccessTest() {
        //given
        Menu menu = saveMenu();
        OrderTable savedOrderTable = saveOrderTableForEmpty(false);
        OrderLineItem orderLineItem = createOrderLineItem(menu);

        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTable(savedOrderTable);

        Order savedOrder = orderService.create(order);

        //when
        Order findOrder = orderRepository.findById(savedOrder.getId()).get();
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(findOrder.getId());

        //then
        assertThat(orderLineItems).hasSize(1);
        assertThat(orderLineItems.get(0))
                .usingRecursiveComparison()
                .ignoringFields("seq")
                .isEqualTo(orderLineItem);
    }

    @DisplayName("변경하려는 주문이 존재하지 않으면 변경할 수 없다.")
    @Test
    void changeOrderStatusFailTest_ByOrderIsNotExists() {
        //given
        Long invalidId = 99L;
        assertThat(orderRepository.findById(invalidId)).isEmpty();

        //when then
        assertThatThrownBy(() -> orderService.changeOrderStatus(invalidId, OrderStatus.COMPLETION))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 완료(COMPLETION) 상태인 경우, 변경할 수 없다.")
    @Test
    void changeOrderStatusFailTest_ByOrderStatusIsCompletion() {
        //given
        Menu menu = saveMenu();
        OrderTable savedOrderTable = saveOrderTableForEmpty(false);
        OrderLineItem orderLineItem = createOrderLineItem(menu);

        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTable(savedOrderTable);
        order.setOrderStatus(OrderStatus.COMPLETION);
        order.setOrderedTime(LocalDateTime.now());

        //when then
        Order otherOrder = new Order();
        Long savedOrderId = orderRepository.save(order).getId();

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrderId, OrderStatus.COMPLETION))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatusSuccessTest() {
        //given
        Menu menu = saveMenu();
        OrderTable savedOrderTable = saveOrderTableForEmpty(false);
        OrderLineItem orderLineItem = createOrderLineItem(menu);

        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTable(savedOrderTable);
        order.setOrderStatus(OrderStatus.COOKING);
        order.setOrderedTime(LocalDateTime.now());

        Long savedOrderId = orderRepository.save(order).getId();

        //when
        orderService.changeOrderStatus(savedOrderId, OrderStatus.COMPLETION);

        //then
        Order findOrder = orderRepository.findById(savedOrderId).get();

        assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    private Menu saveMenu() {
        MenuGroup savedMenuGroup = saveMenuGroup();

        Menu menu = Menu.of("TestMenu", BigDecimal.valueOf(10000), savedMenuGroup);

        return menuRepository.save(menu);
    }

    private MenuGroup saveMenuGroup() {
        MenuGroup menuGroup = MenuGroup.from("TestMenuGroup");

        return menuGroupRepository.save(menuGroup);
    }

    private OrderTable saveOrderTableForEmpty(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);

        return orderTableRepository.save(orderTable);
    }

    private OrderLineItem createOrderLineItem(Menu menu) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenu(menu);

        return orderLineItem;
    }

}
