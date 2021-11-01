package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DisplayName("OrderService를 테스트한다.")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @BeforeEach
    void setUp() {
        orderLineItemDao.deleteAll();
        orderDao.deleteAll();
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        //given
        OrderTable savedOrderTable1 = orderTableDao.save(new OrderTable(null, null, 0, false));
        OrderTable savedOrderTable2 = orderTableDao.save(new OrderTable(null, null, 0, false));

        TableGroup tableGroup = new TableGroup(null, LocalDateTime.now(), Arrays.asList(savedOrderTable1, savedOrderTable2));
        tableGroupDao.save(tableGroup);

        Product savedProduct = productDao.save(new Product(1L, "후라이드 치킨", BigDecimal.valueOf(19000)));
        MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup(1L, "추천 메뉴"));
        MenuProduct menuProduct = new MenuProduct(null, null, savedProduct.getId(), 2L);
        Menu menu = new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(19000), savedMenuGroup.getId(), Collections.singletonList(menuProduct));
        Menu savedMenu = menuDao.save(menu);
        OrderLineItem orderLineItem = new OrderLineItem(null, null, savedMenu.getId(), 1);
        Order order = new Order(null, savedOrderTable1.getId(), null, null, Collections.singletonList(orderLineItem));

        //when
        Order result = orderService.create(order);

        //then
        assertThat(result.getOrderTableId()).isEqualTo(savedOrderTable1.getId());
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.toString());
        OrderLineItem orderLineItemResult = result.getOrderLineItems().get(0);
        assertThat(orderLineItemResult.getMenuId()).isEqualTo(savedMenu.getId());
        assertThat(orderLineItemResult.getQuantity()).isEqualTo(1);
    }

    @DisplayName("주문 등록시 주문 항목이 존재하지 않으면 예외를 던진다.")
    @Test
    void create_not_exist_order_line_item_exception() {
        OrderTable savedOrderTable1 = orderTableDao.save(new OrderTable(null, null, 0, false));
        OrderTable savedOrderTable2 = orderTableDao.save(new OrderTable(null, null, 0, false));
        TableGroup tableGroup = new TableGroup(null, LocalDateTime.now(), Arrays.asList(savedOrderTable1, savedOrderTable2));
        tableGroupDao.save(tableGroup);
        Order order = new Order(null, savedOrderTable1.getId(), null, null, null);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록시 메뉴가 존재하지 않으면 예외를 던진다.")
    @Test
    void create_not_exist_menu_exception() {
        OrderTable savedOrderTable1 = orderTableDao.save(new OrderTable(null, null, 0, false));
        OrderTable savedOrderTable2 = orderTableDao.save(new OrderTable(null, null, 0, false));
        TableGroup tableGroup = new TableGroup(null, LocalDateTime.now(), Arrays.asList(savedOrderTable1, savedOrderTable2));
        tableGroupDao.save(tableGroup);
        OrderLineItem orderLineItem = new OrderLineItem(null, null, null, 1);
        Order order = new Order(null, savedOrderTable1.getId(), null, null, Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록시 주문 테이블이 비어있으면 예외를 던진다.")
    @Test
    void create_not_exist_order_table_exception() {
        OrderTable savedOrderTable1 = orderTableDao.save(new OrderTable(null, null, 0, true));
        OrderTable savedOrderTable2 = orderTableDao.save(new OrderTable(null, null, 0, false));

        TableGroup tableGroup = new TableGroup(null, LocalDateTime.now(), Arrays.asList(savedOrderTable1, savedOrderTable2));
        tableGroupDao.save(tableGroup);

        Product savedProduct = productDao.save(new Product(1L, "후라이드 치킨", BigDecimal.valueOf(19000)));
        MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup(1L, "추천 메뉴"));
        MenuProduct menuProduct = new MenuProduct(null, null, savedProduct.getId(), 2L);
        Menu menu = new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(19000), savedMenuGroup.getId(), Collections.singletonList(menuProduct));
        Menu savedMenu = menuDao.save(menu);
        OrderLineItem orderLineItem = new OrderLineItem(null, null, savedMenu.getId(), 1);
        Order order = new Order(null, savedOrderTable1.getId(), null, null, Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        //given
        OrderTable savedOrderTable1 = orderTableDao.save(new OrderTable(null, null, 0, false));
        OrderTable savedOrderTable2 = orderTableDao.save(new OrderTable(null, null, 0, false));
        TableGroup tableGroup = new TableGroup(null, LocalDateTime.now(), Arrays.asList(savedOrderTable1, savedOrderTable2));
        tableGroupDao.save(tableGroup);
        Product savedProduct = productDao.save(new Product(1L, "후라이드 치킨", BigDecimal.valueOf(19000)));
        MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup(1L, "추천 메뉴"));
        MenuProduct menuProduct = new MenuProduct(null, null, savedProduct.getId(), 2L);
        Menu menu = new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(19000), savedMenuGroup.getId(), Collections.singletonList(menuProduct));
        Menu savedMenu = menuDao.save(menu);
        OrderLineItem orderLineItem = new OrderLineItem(null, null, savedMenu.getId(), 1);
        Order order = new Order(null, savedOrderTable1.getId(), null, null, Collections.singletonList(orderLineItem));
        orderService.create(order);

        //when
        List<Order> results = orderService.list();

        //then
        assertThat(results).hasSize(1);
        Order result = results.get(0);
        assertThat(result.getOrderTableId()).isEqualTo(savedOrderTable1.getId());
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.toString());
        OrderLineItem orderLineItemResult = result.getOrderLineItems().get(0);
        assertThat(orderLineItemResult.getMenuId()).isEqualTo(savedMenu.getId());
        assertThat(orderLineItemResult.getQuantity()).isEqualTo(1);
    }

    @DisplayName("주문 상태를 수정한다.")
    @Test
    void changeOrderStatus() {
        //given
        Order order = createOrder();

        //when
        orderService.changeOrderStatus(order.getId(), new Order(null, null, OrderStatus.MEAL.toString(), null, null));

        //then
        Order result = orderService.list().get(0);
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.toString());
    }

    @DisplayName("주문 상태 수정시 주문이 존재하지 않으면 예외를 던진다.")
    @Test
    void changeOrderStatus_not_exist_order_exception() {
        Order order = new Order(null, null, OrderStatus.MEAL.toString(), null, null);

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 수정시 주문의 상태가 '계산 완료' 상태라면 예외를 던진다.")
    @Test
    void changeOrderStatus_order_status_complete_exception() {
        Order order = createOrder();
        order.setOrderStatus(OrderStatus.COMPLETION.toString());
        Order savedOrder = orderDao.save(order);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), savedOrder)).isInstanceOf(IllegalArgumentException.class);
    }

    private Order createOrder() {
        OrderTable savedOrderTable1 = orderTableDao.save(new OrderTable(null, null, 0, false));
        OrderTable savedOrderTable2 = orderTableDao.save(new OrderTable(null, null, 0, false));
        TableGroup tableGroup = new TableGroup(null, LocalDateTime.now(), Arrays.asList(savedOrderTable1, savedOrderTable2));
        tableGroupDao.save(tableGroup);
        Product savedProduct = productDao.save(new Product(1L, "후라이드 치킨", BigDecimal.valueOf(19000)));
        MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup(1L, "추천 메뉴"));
        MenuProduct menuProduct = new MenuProduct(null, null, savedProduct.getId(), 2L);
        Menu menu = new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(19000), savedMenuGroup.getId(), Collections.singletonList(menuProduct));
        Menu savedMenu = menuDao.save(menu);
        OrderLineItem orderLineItem = new OrderLineItem(null, null, savedMenu.getId(), 1);
        Order order = new Order(null, savedOrderTable1.getId(), null, null, Collections.singletonList(orderLineItem));

        return orderService.create(order);
    }

}