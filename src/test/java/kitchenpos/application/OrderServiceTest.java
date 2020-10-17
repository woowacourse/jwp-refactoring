package kitchenpos.application;

import kitchenpos.config.Dataset;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService service;

    private Menu menu;
    private Order order;
    private OrderLineItem orderItem;
    private OrderTable orderTable;

    @BeforeEach
    public void setUp() {
        Product product = Dataset.product_파스타();
        MenuProduct menuProduct = Dataset.menuProduct_파스타_1_개(product);
        MenuGroup menuGroup = Dataset.menuGroup_양식();
        menu = Dataset.menu_파스타(menuProduct, menuGroup);

        orderItem = Dataset.orderLineItem_파스타_2_개(menu);
        order = Dataset.order_파스타_2_개(orderItem);
        orderTable = Dataset.orderTable_2_명(false);
    }

    @DisplayName("주문 생성 실패 - 주문 아이템이 비었을 때")
    @Test
    public void createFailItemEmpty() {
        order.setOrderLineItems(Lists.newArrayList());

        assertThatThrownBy(() -> service.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 실패 - 주문 아이템이 실제 등록된 메뉴와 다를 때")
    @Test
    public void createFailItemMenuCount() {
        given(menuDao.countByIdIn(any())).willReturn(10L);

        assertThatThrownBy(() -> service.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 실패 - 주문 테이블이 존재하지 않을 때")
    @Test
    public void createFailNotExistedOrderTable() {
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(anyLong())).willThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> service.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 실패 - 주문 테이블이 이미 사용 중일 때")
    @Test
    public void createFailOrderTableEmpty() {
        orderTable.setEmpty(true);
        given(menuDao.countByIdIn(any())).willReturn(1L);

        assertThatThrownBy(() -> service.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성")
    @Test
    public void createOrder() {
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderDao.save(any(Order.class))).willReturn(order);
        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(orderItem);

        final Order savedOrder = service.create(order);

        assertThat(savedOrder.getOrderStatus()).isEqualTo("COOKING");
        assertThat(savedOrder.getOrderTableId()).isEqualTo(7L);
        assertThat(savedOrder.getOrderLineItems()).hasSize(1);
    }

    @DisplayName("주문 목록 조회")
    @Test
    public void readOrders() {
        given(orderDao.findAll()).willReturn(Lists.newArrayList(order));
        given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(Lists.newArrayList(orderItem));

        final List<Order> orders = service.list();

        assertThat(orders).contains(order);
    }

    @DisplayName("주문 상태 변경 - 주문이 없을 때")
    @Test
    public void changeFailNotExistedOrder() {
        given(orderDao.findById(anyLong())).willThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> service.changeOrderStatus(order.getId(), order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 변경 - 식사 완료 상태일 때")
    @Test
    public void changeFailStatusCompletion() {
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(anyLong())).willReturn(Optional.of(order));

        assertThatThrownBy(() -> service.changeOrderStatus(order.getId(), order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 변경")
    @Test
    public void changeOrderStatus() {
        Order newOrder = new Order();
        newOrder.setOrderStatus(OrderStatus.MEAL.name());
        given(orderDao.findById(anyLong())).willReturn(Optional.of(order));

        final Order savedOrder = service.changeOrderStatus(this.order.getId(), newOrder);

        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }
}
