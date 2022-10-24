package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    private Menu menu;
    private OrderTable noEmptyOrderTable;

    @BeforeEach
    void setUp() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("테스트");
        final MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

        final Menu menu = new Menu();
        menu.setPrice(new BigDecimal(0));
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setName("메뉴");
        menu.setMenuProducts(new ArrayList<>());
        this.menu = menuService.create(menu);

        final OrderTable noEmptyOrderTable = new OrderTable();
        noEmptyOrderTable.setEmpty(false);
        this.noEmptyOrderTable = tableService.create(noEmptyOrderTable);
    }

    @Test
    @DisplayName("주문 항목 없는 주문은 있을 수 없다.")
    void createNoOrderLineItems() {
        final Order order = new Order();
        order.setOrderTableId(noEmptyOrderTable.getId());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴는 주문할 수 없다.")
    void createNoExistMenu() {
        final Order order = new Order();
        order.setOrderTableId(noEmptyOrderTable.getId());
        order.setOrderedTime(LocalDateTime.now());
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(9999L);
        orderLineItem.setQuantity(10);
        order.setOrderLineItems(List.of(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 테이블에선 주문할 수 없다.")
    void createNoExistTable() {
        final Order order = new Order();
        order.setOrderTableId(1818181818L);
        order.setOrderedTime(LocalDateTime.now());
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(10);
        order.setOrderLineItems(List.of(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 저장한다.")
    void create() {
        final Order order = new Order();
        order.setOrderTableId(noEmptyOrderTable.getId());
        order.setOrderedTime(LocalDateTime.now());
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(10);
        order.setOrderLineItems(List.of(orderLineItem));

        final Order savedOrder = orderService.create(order);

        final List<OrderLineItem> savedOrderLineItems = orderLineItemDao.findAllByOrderId(savedOrder.getId());

        assertAll(
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrderLineItems).usingElementComparatorIgnoringFields("seq", "orderId")
                        .contains(orderLineItem)
        );
    }

    @Test
    @DisplayName("주문의 상태를 변경한다.")
    void changeOrderStatus() {
        final Order order = new Order();
        order.setOrderTableId(noEmptyOrderTable.getId());
        order.setOrderedTime(LocalDateTime.now());
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(10);
        order.setOrderLineItems(List.of(orderLineItem));

        final Order savedOrder = orderService.create(order);

        savedOrder.setOrderStatus(OrderStatus.MEAL.name());

        final Order changedOrderStatus = orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        assertThat(changedOrderStatus.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("주문의 상태가 완료상태면 상태를 변경할 수 없다.")
    void changeOrderStatusComplete() {
        final Order order = new Order();
        order.setOrderTableId(noEmptyOrderTable.getId());
        order.setOrderedTime(LocalDateTime.now());
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(10);
        order.setOrderLineItems(List.of(orderLineItem));

        final Order savedOrder = orderService.create(order);

        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        final Order changedOrderStatus = orderService.changeOrderStatus(savedOrder.getId(), savedOrder);
        changedOrderStatus.setOrderStatus(OrderStatus.MEAL.name());
        assertThatThrownBy(() -> orderService.changeOrderStatus(changedOrderStatus.getId(), changedOrderStatus))
                .isInstanceOf(IllegalArgumentException.class);
    }
}