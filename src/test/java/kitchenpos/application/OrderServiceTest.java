package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
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

@SpringBootTest
@Transactional
@Sql("/schema.sql")
class OrderServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private MenuGroup menuGroup;
    private Menu menu;
    private OrderTable orderTable;
    private Order order;

    @BeforeEach
    void setUp() {
        menuGroupDao.save(generateMenuGroup());
        menuDao.save(generateMenu());
        orderTable = tableService.create(3, false);
    }

    @Test
    @DisplayName("주문을 생성한다")
    void create() {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        final OrderLineItem orderLineItem = generateOrderLineItem(1L, 1);
        orderLineItems.add(orderLineItem);
        order = generateOrder(OrderStatus.COOKING.name(), orderLineItems);
        final Order createOrder = orderService.create(order);

        assertThat(createOrder.getOrderTableId())
                .isEqualTo(orderTable.getId());
    }

    @Test
    @DisplayName("메뉴가 없는 주문을 생성하면 예외를 반환한다")
    void create_notHaveMenuException() {
        order = generateOrder(OrderStatus.COOKING.name(), new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴로 주문을 생성하면 예외를 반환한다")
    void create_notExistMenuException() {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        final OrderLineItem orderLineItem = generateOrderLineItem(999999999L, 1);
        orderLineItems.add(orderLineItem);
        order = generateOrder(OrderStatus.COOKING.name(), orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 전체를 조회한다")
    void list() {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        final OrderLineItem orderLineItem = generateOrderLineItem(1L, 1);
        orderLineItems.add(orderLineItem);
        order = generateOrder(OrderStatus.COOKING.name(), orderLineItems);
        orderService.create(order);

        final List<Order> actual = orderService.list();

        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual)
                        .extracting("orderTableId")
                        .containsExactly(orderTable.getId())
        );
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    void changeOrderStatus() {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        final OrderLineItem orderLineItem = generateOrderLineItem(1L, 1);
        orderLineItems.add(orderLineItem);
        final Order createOrder = orderService.create(generateOrder(OrderStatus.COOKING.name(), orderLineItems));

        final Order order2 = new Order(1L, OrderStatus.MEAL.name(), LocalDateTime.now());

        final Order actual = orderService.changeOrderStatus(createOrder.getId(), order2);

        assertThat(actual.getOrderStatus())
                .isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("`계산 완료`인 주문 상태를 변경하면 예외를 반환한다")
    void changeOrderStatus_completionException() {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        final OrderLineItem orderLineItem = generateOrderLineItem(1L, 2);
        orderLineItems.add(orderLineItem);
        order = generateOrder(OrderStatus.COOKING.name(), orderLineItems);

        final Order order2 = new Order(1L, OrderStatus.MEAL.name(), LocalDateTime.now());

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Order generateOrder(final String orderStatus, final List<OrderLineItem> orderLineItems) {
        return new Order(orderTable.getId(), orderStatus, LocalDateTime.now(), orderLineItems);
    }

    private OrderLineItem generateOrderLineItem(final Long menuId, final int quantity) {
        return new OrderLineItem(1L, 1L, menuId, quantity);
    }

    private MenuGroup generateMenuGroup() {
        menuGroup = new MenuGroup(1L, "애기메뉴들");
        return menuGroup;
    }

    private Menu generateMenu() {
        menu = new Menu(1L, "애기메뉴", BigDecimal.valueOf(20_000L), 1L, new ArrayList<>());
        return menu;
    }
}
