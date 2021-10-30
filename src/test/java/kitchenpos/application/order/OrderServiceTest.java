package kitchenpos.application.order;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    protected List<Order> standardOrders;
    protected List<OrderLineItem> standardOrderLineItems;
    protected Order standardOrder;
    protected OrderTable standardOrderTable;

    @Mock
    protected MenuDao menuDao;

    @Mock
    protected OrderDao orderDao;

    @Mock
    protected OrderLineItemDao orderLineItemDao;

    @Mock
    protected OrderTableDao orderTableDao;

    @InjectMocks
    protected OrderService orderService;

    @BeforeEach
    protected void setUp() {
        standardOrder = new Order();
        standardOrder.setId(1L);
        standardOrder.setOrderTableId(1L);
        standardOrder.setOrderStatus(OrderStatus.COOKING.name());
        standardOrder.setOrderedTime(LocalDateTime.now());

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(1L);
        orderLineItem.setOrderId(1L);
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1L);
        standardOrderLineItems = new LinkedList<>();
        standardOrderLineItems.add(orderLineItem);

        standardOrders = new LinkedList<>();
        standardOrders.add(standardOrder);
        standardOrder.setOrderLineItems(standardOrderLineItems);

        standardOrderTable = new OrderTable();
        standardOrderTable.setId(1L);
        standardOrderTable.setTableGroupId(1L);
    }
}