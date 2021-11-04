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

    protected static final Integer BASIC_SIZE = 1;
    protected static final Long BASIC_COUNT_NUMBER = 1L;
    protected static final Long BASIC_MENU_ID = 1L;
    protected static final Long BASIC_ORDER_ID = 1L;
    protected static final Long BASIC_ORDER_TABLE_ID = 1L;
    protected static final Long BASIC_QUANTITY = 1L;
    protected static final Long BASIC_SEQUENCE_NUMBER = 1L;
    protected static final Long BASIC_TABLE_GROUP_ID = 1L;

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
        standardOrder.setId(BASIC_ORDER_ID);
        standardOrder.setOrderTableId(BASIC_ORDER_TABLE_ID);
        standardOrder.setOrderStatus(OrderStatus.COOKING.name());
        standardOrder.setOrderedTime(LocalDateTime.now());

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(BASIC_SEQUENCE_NUMBER);
        orderLineItem.setOrderId(BASIC_ORDER_ID);
        orderLineItem.setMenuId(BASIC_MENU_ID);
        orderLineItem.setQuantity(BASIC_QUANTITY);
        standardOrderLineItems = new LinkedList<>();
        standardOrderLineItems.add(orderLineItem);

        standardOrders = new LinkedList<>();
        standardOrders.add(standardOrder);
        standardOrder.setOrderLineItems(standardOrderLineItems);

        standardOrderTable = new OrderTable();
        standardOrderTable.setId(BASIC_ORDER_TABLE_ID);
        standardOrderTable.setTableGroupId(BASIC_TABLE_GROUP_ID);
    }

}