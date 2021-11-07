package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixtures.OrderFixtures;
import kitchenpos.fixtures.TableFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class OrderServiceTest extends ServiceTest {

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

    private Order order;

    @BeforeEach
    void setUp() {
        order = OrderFixtures.createOrder();
    }

    @Test
    void 주문을_생성한다() {
        given(menuDao.countByIdIn(any()))
            .willReturn(Long.valueOf(order.getOrderLineItems().size()));
        given(orderTableDao.findById(any())).willReturn(Optional.of(TableFixtures.createOrderTable(false)));
        given(orderDao.save(any())).willReturn(order);
        given(orderLineItemDao.save(any())).willReturn(OrderFixtures.createOrderLineItem());

        assertDoesNotThrow(() -> orderService.create(OrderFixtures.createOrder()));
        verify(orderLineItemDao, times(order.getOrderLineItems().size())).save(any());
        verify(orderDao, times(1)).save(any());
    }

    @Test
    void 생성_시_주문_항목이_비어있으면_예외를_반환한다() {
        Order emptyOrder = OrderFixtures.createOrder(
            1L, 1L, "COOKING", Collections.emptyList()
        );

        assertThrows(IllegalArgumentException.class, () -> orderService.create(emptyOrder));
    }

    @Test
    void 생성_시_주문_항목의_수와_메뉴의_수가_일치하지_않으면_예외를_반환한다() {
        given(menuDao.countByIdIn(any()))
            .willReturn(Long.valueOf(order.getOrderLineItems().size() + 1));

        assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
    }

    @Test
    void 생성_시_주문_테이블이_존재하지_않으면_예외를_반환한다() {
        given(menuDao.countByIdIn(any()))
            .willReturn(Long.valueOf(order.getOrderLineItems().size()));
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
    }

    @Test
    void 생성_시_주문_테이블이_빈_테이블이면_예외를_반환한다() {
        OrderTable emptyTable = TableFixtures.createOrderTable(true);
        given(menuDao.countByIdIn(any()))
            .willReturn(Long.valueOf(order.getOrderLineItems().size()));
        given(orderTableDao.findById(any())).willReturn(Optional.of(emptyTable));

        assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
    }

    @Test
    void 주문_리스트를_반환한다() {
        given(orderDao.findAll()).willReturn(Collections.singletonList(order));
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(
            Collections.singletonList(OrderFixtures.createOrderLineItem())
        );

        List<Order> orders = assertDoesNotThrow(() -> orderService.list());
        orders.stream()
            .map(Order::getOrderLineItems)
            .forEach(lineItems -> assertThat(lineItems).isNotEmpty());
    }

    @Test
    void 주문_상태를_변경한다() {
        Order completedOrder = OrderFixtures.createOrder(OrderStatus.COMPLETION.name());
        given(orderDao.findById(any())).willReturn(Optional.of(order));
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(OrderFixtures.createOrderLineItems());

        Order changedOrder = assertDoesNotThrow(() -> orderService.changeOrderStatus(this.order.getId(), completedOrder));
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    void 상태_변경_시_주문이_존재하지_않으면_예외를_반환한다() {
        Order completedOrder = OrderFixtures.createOrder(OrderStatus.COMPLETION.name());
        given(orderDao.findById(any())).willReturn(Optional.empty());

        assertThrows(
            IllegalArgumentException.class,
            () -> orderService.changeOrderStatus(this.order.getId(), completedOrder)
        );
    }

    @Test
    void 상태_변경_시_완료된_주문_변경_시_예외를_반환한다() {
        Order completedOrder = OrderFixtures.createOrder(OrderStatus.COMPLETION.name());
        given(orderDao.findById(any())).willReturn(Optional.of(completedOrder));

        assertThrows(
            IllegalArgumentException.class,
            () -> orderService.changeOrderStatus(this.order.getId(), order)
        );
    }
}