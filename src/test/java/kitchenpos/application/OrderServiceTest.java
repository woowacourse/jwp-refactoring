package kitchenpos.application;

import static java.util.Collections.emptyList;
import static kitchenpos.fixture.MenuFixture.createMenu;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.OrderFixture.createOrder;
import static kitchenpos.fixture.OrderFixture.createOrderRequest;
import static kitchenpos.fixture.OrderFixture.createOrderRequestChangeOrderStatus;
import static kitchenpos.fixture.OrderLineItemFixture.createOrderLineItemRequest;
import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderServiceTest extends AbstractServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    OrderTable orderTable;

    MenuGroup menuGroup;

    Menu menu;

    @BeforeEach
    void setup() {
        orderTable = orderTableDao
            .save(orderTable = orderTableDao.save(createOrderTable(null, false, 0, null)));
        menuGroup = menuGroupDao.save(createMenuGroup(null, "메뉴그룹"));
        menu = menuDao.save(createMenu(null, "메뉴", 0L, menuGroup.getId()));
    }

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void create() {
        Order order = createOrderRequest(orderTable.getId(),
            Arrays.asList(createOrderLineItemRequest(menu.getId(), 1)));

        Order savedOrder = orderService.create(order);

        assertAll(
            () -> assertThat(savedOrder.getId()).isNotNull(),
            () -> assertThat(savedOrder)
                .isEqualToIgnoringGivenFields(order, "id", "orderLineItems"),
            () -> assertThat(savedOrder.getOrderLineItems())
                .usingElementComparatorIgnoringFields("seq")
                .isEqualTo(order.getOrderLineItems())
        );
    }

    @DisplayName("주문 항목이 0개 이하면 주문을 생성할 수 없다.")
    @Test
    void create_throws_exception() {
        Order order = createOrderRequest(orderTable.getId(), emptyList());

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문 항목과 메뉴의 개수가 같지 않으면 주문을 생성할 수 없다.")
    @Test
    void create_throws_exception2() {
        Order order = createOrderRequest(
            orderTable.getId(),
            Arrays.asList(
                createOrderLineItemRequest(menu.getId(), 1),
                createOrderLineItemRequest(menu.getId(), 1)
            )
        );

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문 테이블이 존재하지 않으면 주문을 생성할 수 없다.")
    @Test
    void create_throws_exception3() {
        Order order = createOrderRequest(
            orderTable.getId() + 1,
            Arrays.asList(createOrderLineItemRequest(menu.getId(), 1))
        );

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("빈 테이블은 주문을 생성할 수 없다.")
    @Test
    void create_throws_exception4() {
        orderTable = orderTableDao
            .save(orderTable = orderTableDao.save(createOrderTable(null, true, 0, null)));
        Order order = createOrderRequest(
            orderTable.getId(),
            Arrays.asList(createOrderLineItemRequest(menu.getId(), 1))
        );

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        List<Order> savedOrders = Arrays.asList(
            orderDao.save(createOrder(null, LocalDateTime.now(), null, OrderStatus.COOKING,
                orderTable.getId())),
            orderDao.save(createOrder(null, LocalDateTime.now(), null, OrderStatus.COOKING,
                orderTable.getId())),
            orderDao.save(createOrder(null, LocalDateTime.now(), null, OrderStatus.COOKING,
                orderTable.getId()))
        );

        List<Order> allOrders = orderService.list();

        assertThat(allOrders).usingElementComparatorIgnoringFields("orderLineItems")
            .containsAll(savedOrders);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        Order order = orderDao
            .save(createOrder(null, LocalDateTime.now(), null, OrderStatus.COOKING,
                orderTable.getId()));
        Order orderRequest = createOrderRequestChangeOrderStatus(OrderStatus.MEAL);

        Order changedOrder = orderService.changeOrderStatus(order.getId(), orderRequest);

        assertThat(changedOrder.getOrderStatus()).isEqualTo(orderRequest.getOrderStatus());
    }

    @DisplayName("완료된 주문의 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatus_throws_exception() {
        Order order = orderDao
            .save(createOrder(null, LocalDateTime.now(), null, OrderStatus.COMPLETION,
                orderTable.getId()));
        Order orderRequest = createOrderRequestChangeOrderStatus(OrderStatus.COOKING);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderRequest));
    }
}
