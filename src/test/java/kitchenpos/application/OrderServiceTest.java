package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.order.application.dto.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderRequest;
import kitchenpos.order.application.dto.OrderRequest.Status;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.menu.domain.repository.MenuDao;
import kitchenpos.menu.domain.repository.MenuGroupDao;
import kitchenpos.order.domain.repository.OrderDao;
import kitchenpos.order.domain.repository.OrderLineItemDao;
import kitchenpos.table.domain.repository.OrderTableDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderDao orderDao;

    private Order order;

    @BeforeEach
    void setUp() {
        Menu menu = createMenu();
        OrderRequest.Create request = createOrderRequest(menu.getId());
        order = orderDao.save(Order.create(request.getOrderTableId()));
        orderLineItemDao.save(new OrderLineItem(order.getId(), menu.getId(), 3L));
    }

    private Menu createMenu() {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("A그룹"));
        return menuDao.save(new Menu("신메뉴", BigDecimal.ONE, menuGroup.getId(), List.of()));
    }

    private OrderRequest.Create createOrderRequest(Long menuId) {
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 1, false));

        return new OrderRequest.Create(orderTable.getId(), List.of(new OrderLineItemRequest.Create(menuId, 1)));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        OrderResponse actual = orderService.create(createOrderRequest(createMenu().getId()));

        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("주문 항목이 없으면 예외가 발생한다.")
    @Test
    void createFailureWhenNotExistsOrderLineItems() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 1, false));
        final List<OrderLineItemRequest.Create> emptyOrderLineItem = Collections.emptyList();

        assertThatThrownBy(() -> orderService.create(new OrderRequest.Create(orderTable.getId(), emptyOrderLineItem)));
    }

    @DisplayName("주문 항목의 메뉴가 디비안에 없으면 예외가 발생한다")
    @Test
    void createFailureWhenNotExistsMenu() {
        OrderRequest.Create order = createOrderRequest(null);

        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있으면 예외가 발생한다")
    @Test
    void createFailureWhenNotOrderTableIsEmpty() {
        Menu menu = createMenu();
        final Long emptyOrderTableId = null;

        OrderRequest.Create request = new OrderRequest.Create(emptyOrderTableId,
                List.of(new OrderLineItemRequest.Create(menu.getId(), 1)));

        assertThatThrownBy(() -> orderService.create(request))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록들을 반환한다.")
    @Test
    void list() {
        assertThat(orderService.list()).hasSize(1);
    }

    @DisplayName("주문의 상태를 바꾼다.")
    @Test
    void changeOrderStatus() {
        OrderRequest.Status request = new OrderRequest.Status(OrderStatus.COOKING);

        orderService.changeOrderStatus(order.getId(), request);
        Order findOrder = orderDao.findById(order.getId()).get();

        assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("기존의 주문 상태가 COMPLETION일 때, 주문 상태를 바꾸려고 하면 예외가 발생한다")
    @Test
    void changeOrderStatusFailureWhenOrderStatusIsCOMPLETION() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 1, false));
        Order order = orderDao.save(new Order(orderTable.getId(), OrderStatus.COMPLETION, LocalDateTime.now()));

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new Status(OrderStatus.COOKING)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
