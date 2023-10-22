package kitchenpos.application;

import static kitchenpos.application.fixture.MenuFixture.createMenu;
import static kitchenpos.application.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.application.fixture.OrderFixture.createOrder;
import static kitchenpos.application.fixture.OrderLineItemFixture.createOrderLineItem;
import static kitchenpos.application.fixture.OrderTableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private static final MenuGroup MENU_GROUP = createMenuGroup(1L, "menuGroup");
    private static final OrderTable ORDER_TABLE = createOrderTable(1L, 3);

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("새 주문을 저장한다.")
    @Test
    void create_success() {
        // given
        final Order order = createOrder(1L, ORDER_TABLE.getId());
        final Menu menu = createMenu(1L, "menu", 1000L, MENU_GROUP.getId());
        final OrderLineItem orderLineItem = createOrderLineItem(order.getId(), menu.getId(), 3);
        order.addOrderLineItems(List.of(orderLineItem));

        given(menuDao.countByIdIn(anyList()))
            .willReturn(1L);
        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.of(ORDER_TABLE));
        given(orderDao.save(any(Order.class)))
            .willReturn(order);
        given(orderLineItemDao.save(any(OrderLineItem.class)))
            .willReturn(orderLineItem);

        // when
        final Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder).usingRecursiveComparison()
            .isEqualTo(order);
    }

    @DisplayName("주문 저장 시, 메뉴가 비어있으면 예외가 발생한다.")
    @Test
    void create_empty_fail() {
        // given
        final Order order = createOrder(1L, ORDER_TABLE.getId());
        final Menu menu = createMenu(1L, "menu", 1000L, MENU_GROUP.getId());
        final OrderLineItem orderLineItem = createOrderLineItem(order.getId(), menu.getId(), 3);
        order.addOrderLineItems(List.of(orderLineItem));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 저장 시, 존재하지 않는 메뉴가 있다면 예외가 발생한다.")
    @Test
    void create_notExistMenu_fail() {
        // given
        final Order order = createOrder(1L, ORDER_TABLE.getId());
        final OrderLineItem orderLineItem = createOrderLineItem(order.getId(), 0L, 3);
        order.addOrderLineItems(List.of(orderLineItem));

        given(menuDao.countByIdIn(anyList()))
            .willReturn(0L);

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 저장 시, 주문 테이블이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void create_notExistTable_fail() {
        // given
        final Order order = createOrder(1L, 0L);
        final OrderLineItem orderLineItem = createOrderLineItem(order.getId(), 0L, 3);
        order.addOrderLineItems(List.of(orderLineItem));

        given(menuDao.countByIdIn(anyList()))
            .willReturn(1L);
        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 저장 시, 주문 테이블이 비어있다면 예외가 발생한다.")
    @Test
    void create_emptyTable_fail() {
        // given
        final OrderTable orderTable = createOrderTable(1L, 3);
        orderTable.updateEmpty(true);

        final Order order = createOrder(1L, orderTable.getId());
        final OrderLineItem orderLineItem = createOrderLineItem(order.getId(), 0L, 3);
        order.addOrderLineItems(List.of(orderLineItem));

        given(menuDao.countByIdIn(anyList()))
            .willReturn(1L);
        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.of(orderTable));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 Order 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final Order order1 = createOrder(1L, ORDER_TABLE.getId());
        final Menu menu = createMenu(1L, "menuGroup", 1000L, MENU_GROUP.getId());
        final OrderLineItem orderLineItem1 = createOrderLineItem(order1.getId(), menu.getId(), 3);
        order1.addOrderLineItems(List.of(orderLineItem1));
        final Order order2 = createOrder(2L, ORDER_TABLE.getId());
        final OrderLineItem orderLineItem2 = createOrderLineItem(order2.getId(), menu.getId(), 3);
        order2.addOrderLineItems(List.of(orderLineItem2));


        given(orderDao.findAll())
            .willReturn(List.of(order1, order2));

        // when
        final List<Order> result = orderService.list();

        // then
        assertThat(result).usingRecursiveComparison()
            .isEqualTo(List.of(order1, order2));
    }

    @DisplayName("Order 상태를 바꾼다.")
    @Test
    void changeOrderStatus() {
        // given
        final Order prevOrder = createOrder(1L, ORDER_TABLE.getId());
        final Order changeOrder = createOrder(1L, ORDER_TABLE.getId());
        changeOrder.changeOrderStatus(OrderStatus.MEAL);

        given(orderDao.findById(anyLong()))
            .willReturn(Optional.of(prevOrder));

        // when
        final Order changedOrder = orderService.changeOrderStatus(prevOrder.getId(), changeOrder);

        // then
        assertThat(changedOrder.getId()).isEqualTo(1L);
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("Order 상태를 바꿀 때, 존재하지 않는 주문이라면 예외가 발생한다.")
    @Test
    void changeOrderStatus_notExist_fail() {
        // given
        final Order order = createOrder(1L, ORDER_TABLE.getId());
        given(orderDao.findById(anyLong()))
            .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order 상태를 바꿀 때, 상태가 completion 이라면 예외가 발생한다.")
    @Test
    void changeOrderStatus_wrongStatus_fail() {
        // given
        final Order order = createOrder(1L, ORDER_TABLE.getId());
        order.changeOrderStatus(OrderStatus.COMPLETION);

        final Order changeOrder = createOrder(1L, ORDER_TABLE.getId());
        changeOrder.changeOrderStatus(OrderStatus.MEAL);

        given(orderDao.findById(anyLong()))
            .willReturn(Optional.of(order));

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, changeOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
