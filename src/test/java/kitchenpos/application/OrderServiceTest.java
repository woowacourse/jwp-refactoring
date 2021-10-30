package kitchenpos.application;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.Fixtures;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    private OrderLineItem orderLineItem;
    private List<OrderLineItem> orderLineItems;
    private Order order;
    private OrderTable orderTable;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        orderLineItem = Fixtures.makeOrderLineItem();

        orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);

        order = Fixtures.makeOrder();
        order.setOrderLineItems(orderLineItems);

        orderTable = Fixtures.makeOrderTable();
        orderTable.setEmpty(false);

    }

    @DisplayName("order 생성")
    @Test
    void create() {
        given(menuDao.countByIdIn(anyList()))
            .willReturn(1L);
        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.of(orderTable));
        given(orderDao.save(order))
            .willReturn(order);

        orderService.create(order);

        verify(orderDao).save(order);
        verify(orderLineItemDao).save(orderLineItem);
    }

    @DisplayName("order 불러오기")
    @Test
    void list() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        given(orderDao.findAll())
            .willReturn(orders);

        orderService.list();

        verify(orderDao).findAll();
        verify(orderLineItemDao).findAllByOrderId(anyLong());
    }

    @DisplayName("주문상태 바꾸기")
    @Test
    void changeOrder() {
        given(orderDao.findById(anyLong()))
            .willReturn(Optional.of(order));

        orderService.changeOrderStatus(1L, order);

        verify(orderDao).save(order);
        verify(orderLineItemDao).findAllByOrderId(anyLong());
    }

}
