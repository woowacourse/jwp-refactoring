package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("1 개 이상의 등록된 메뉴로 주문을 등록할 수 있다.")
    @Test
    void create() {
        //given
        OrderTable orderTable = orderTableDao.save(new OrderTable(1, false));
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("한마리 메뉴"));
        Product product = productDao.save(new Product("간장치킨", 10000L));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 2);
        Menu menu = menuDao.save(new Menu("간장 치킨 두마리", 19000L, menuGroup.getId(), Collections.singletonList(menuProduct)));

        OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 3);
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.singletonList(orderLineItem));

        //when
        Order savedOrder = orderService.create(order);

        //then
        Order findOrder = orderDao.findById(savedOrder.getId())
                .orElseThrow(RuntimeException::new);
        List<OrderLineItem> findOrderLineItems = orderLineItemDao.findAllByOrderId(findOrder.getId());

        assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(findOrderLineItems).hasSize(1);
    }

    @DisplayName("주문 항목 없이 주문을 할 수 없다.")
    @Test
    void createException1() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(1, false));
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목 없이 주문을 할 수 없습니다.");
    }

    @DisplayName("존재하지 않는 테이블에 주문을 할 수 없다.")
    @Test
    void createException2() {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("한마리 메뉴"));
        Product product = productDao.save(new Product("간장치킨", 10000L));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 2);
        Menu menu = menuDao.save(new Menu("간장 치킨 두마리", 19000L, menuGroup.getId(), Collections.singletonList(menuProduct)));

        OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 3);
        Order order = new Order(null, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테이블에 주문을 할 수 없습니다.");
    }

    @DisplayName("빈 테이블에는 주문을 등록할 수 없다.")
    @Test
    void createException3() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(0, true));
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("한마리 메뉴"));
        Product product = productDao.save(new Product("간장치킨", 10000L));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 2);
        Menu menu = menuDao.save(new Menu("간장 치킨 두마리", 19000L, menuGroup.getId(), Collections.singletonList(menuProduct)));

        OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 3);
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블에는 주문을 등록할 수 없습니다.");
    }

    @DisplayName("존재하지 않는 메뉴로 주문을 할 수 없다.")
    @Test
    void createException4() {
        //given
        OrderTable orderTable = orderTableDao.save(new OrderTable(1, false));
        Product product = productDao.save(new Product("간장치킨", 10000L));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 2);
        Menu menu = new Menu("간장 치킨 두마리", 19000L, 1L, Collections.singletonList(menuProduct));

        OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 3);
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.singletonList(orderLineItem));

        //when
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 메뉴로 주문을 할 수 없습니다.");
    }

    @DisplayName("주문의 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        OrderTable orderTable = orderTableDao.save(new OrderTable(1, false));
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("한마리 메뉴"));
        Product product = productDao.save(new Product("간장치킨", 10000L));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 2);
        Menu menu = menuDao.save(new Menu("간장 치킨 두마리", 19000L, menuGroup.getId(), Collections.singletonList(menuProduct)));

        OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 3);
        Order savedOrder = orderDao.save(new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.singletonList(orderLineItem)));
        orderLineItem.setOrderId(savedOrder.getId());
        orderLineItemDao.save(orderLineItem);

        //when
        List<Order> orders = orderService.list();

        //then
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getOrderLineItems()).hasSize(1);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"MEAL", "COOKING", "COMPLETION"})
    void changeOrderStatus(OrderStatus orderStatus) {
        //given
        OrderTable orderTable = orderTableDao.save(new OrderTable(1, false));
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("한마리 메뉴"));
        Product product = productDao.save(new Product("간장치킨", 10000L));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 2);
        Menu menu = menuDao.save(new Menu("간장 치킨 두마리", 19000L, menuGroup.getId(), Collections.singletonList(menuProduct)));

        OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 3);
        Order savedOrder = orderDao.save(new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.singletonList(orderLineItem)));
        orderLineItem.setOrderId(savedOrder.getId());
        orderLineItemDao.save(orderLineItem);

        //when
        Order order = new Order(savedOrder.getOrderTableId(), orderStatus.name(), LocalDateTime.now(), new ArrayList<>());
        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), order);

        //then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(orderStatus.name());
    }

    @DisplayName("주문 상태가 계산 완료인 경우 변경할 수 없다.")
    @ParameterizedTest
    @CsvSource(value = {"MEAL", "COOKING", "COMPLETION"})
    void changeOrderStatusException1(OrderStatus orderStatus) {
        //given
        OrderTable orderTable = orderTableDao.save(new OrderTable(1, false));
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("한마리 메뉴"));
        Product product = productDao.save(new Product("간장치킨", 10000L));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 2);
        Menu menu = menuDao.save(new Menu("간장 치킨 두마리", 19000L, menuGroup.getId(), Collections.singletonList(menuProduct)));

        OrderLineItem orderLineItem = new OrderLineItem(null, menu.getId(), 3);
        Order savedOrder = orderDao.save(new Order(orderTable.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), Collections.singletonList(orderLineItem)));
        orderLineItem.setOrderId(savedOrder.getId());
        orderLineItemDao.save(orderLineItem);

        //when
        Order order = new Order(savedOrder.getOrderTableId(), orderStatus.name(), LocalDateTime.now(), new ArrayList<>());

        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 계산 완료인 경우 변경할 수 없습니다.");
    }

    @DisplayName("존재하지 않는 주문을 변경할 수 없다.")
    @Test
    void changeOrderStatusException2() {
        Order order = new Order(null, OrderStatus.MEAL.name(), LocalDateTime.now(), new ArrayList<>());

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문을 변경할 수 없습니다.");
    }

    @AfterEach
    void tearDown() {
        orderLineItemDao.deleteAll();
        orderDao.deleteAll();
        orderTableDao.deleteAll();
        menuDao.deleteAll();
        menuGroupDao.deleteAll();
        menuProductDao.deleteAll();
        productDao.deleteAll();
    }
}