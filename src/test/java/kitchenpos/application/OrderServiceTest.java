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
        orderTable = tableService.create(new OrderTable());
        order = generateOrder(OrderStatus.COOKING.name(), new ArrayList<>());
    }


    @Test
    @DisplayName("주문을 생성한다")
    void create() {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        final OrderLineItem orderLineItem = generateOrderLineItem(1L, 1);
        orderLineItems.add(orderLineItem);
        order.setOrderLineItems(orderLineItems);

        final Order createOrder = orderService.create(order);

        assertThat(createOrder.getOrderTableId())
                .isEqualTo(orderTable.getId());
    }

    @Test
    @DisplayName("메뉴가 없는 주문을 생성하면 예외를 반환한다")
    void create_notHaveMenuException() {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        order.setOrderLineItems(orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴로 주문을 생성하면 예외를 반환한다")
    void create_notExistMenuException() {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        final OrderLineItem orderLineItem = generateOrderLineItem(999999999L, 1);
        orderLineItems.add(orderLineItem);
        order.setOrderLineItems(orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 전체를 조회한다")
    void list() {
        orderDao.save(order);

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
        order.setOrderLineItems(orderLineItems);
        final Order createOrder = orderService.create(order);

        final Order order2 = new Order();
        order2.setOrderStatus(OrderStatus.MEAL.name());

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
        order.setOrderLineItems(orderLineItems);
        orderService.create(order);
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        final Order order2 = new Order();
        order2.setOrderStatus(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Order generateOrder(final String orderStatus, final List<OrderLineItem> orderLineItems) {
        order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    private OrderLineItem generateOrderLineItem(final Long menuId, final int quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(1L);
        orderLineItem.setOrderId(order.getId());
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    private MenuGroup generateMenuGroup() {
        menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("애기메뉴들");
        return menuGroup;
    }

    private Menu generateMenu() {
        menu = new Menu();
        menu.setId(1L);
        menu.setName("애기메뉴");
        menu.setPrice(BigDecimal.valueOf(20_000L));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(new ArrayList<>());
        return menu;
    }
}
