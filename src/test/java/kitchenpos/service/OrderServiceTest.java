package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.application.OrderService;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderLineItemDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class OrderServiceTest extends ServiceTest {
    @Mock
    private JdbcTemplateMenuDao menuDao;
    @Mock
    private JdbcTemplateOrderTableDao orderTableDao;
    @Mock
    private JdbcTemplateOrderDao orderDao;
    @Mock
    private JdbcTemplateOrderLineItemDao orderLineItemDao;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문을 등록 할 수 있다.")
    @Test
    void create() {
        when(menuDao.countByIdIn(any())).thenReturn(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(OrderTableFixture.orderTable()));
        when(orderDao.save(any())).thenReturn(OrderFixture.order());
        when(orderLineItemDao.save(any())).thenReturn(OrderLineItemFixture.orderLineItem());
        Order order = OrderFixture.order();

        orderService.create(order);
    }

    @DisplayName("주문시 주문 항목이 empty 일 수 없다.")
    @Test
    void createEmpty() {
        Order order = new Order(0L, 0L, OrderStatus.MEAL.name(), LocalDateTime.now(), Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 시 주문 항목이 모두 등록되어 있어야 한다")
    @Test
    void createNotRegisteredIds() {
        when(menuDao.countByIdIn(any())).thenReturn(0L);
        Order order = OrderFixture.order();

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 등록되어 있어야 한다")
    @Test
    void createNotRegisteredOrderTable() {
        when(menuDao.countByIdIn(any())).thenReturn(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());
        Order order = OrderFixture.order();

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문들을 조회할 수 있다.")
    @Test
    void findAll() {
        when(orderDao.findAll()).thenReturn(Arrays.asList());

        orderService.list();
    }

    @DisplayName("주문 상태의 변화가 가능하다")
    @Test
    void change() {
        when(orderDao.findById(any())).thenReturn(Optional.of(OrderFixture.order()));
        when(orderDao.save(any())).thenReturn(OrderFixture.order());
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(OrderLineItemFixture.orderLineItem()));

        orderService.changeOrderStatus(0L, OrderFixture.order());
    }

    @DisplayName("주문 상태의 변화시 주문 Id가 없으면 변경할 수 없다")
    @Test
    void changeInvalidOrderId() {
        when(orderDao.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(0L, OrderFixture.order())).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("계산 완료인 주문은 수정할 수 없다.")
    @Test
    void changeCompletionOrder() {
        Order completionOrder = new Order(0L, 0L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), Arrays.asList());
        when(orderDao.findById(any())).thenReturn(Optional.of(completionOrder));

        assertThatThrownBy(() -> orderService.changeOrderStatus(0L, OrderFixture.order())).isInstanceOf(
                IllegalArgumentException.class);
    }
}
