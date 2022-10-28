package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;

import java.time.LocalDateTime;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ServiceTest {

    @MockBean(name = "productDao")
    protected ProductDao productDao;

    @MockBean(name = "menuGroupDao")
    protected MenuGroupDao menuGroupDao;

    @MockBean(name = "menuDao")
    protected MenuDao menuDao;

    @MockBean(name = "orderTableDao")
    protected OrderTableDao orderTableDao;

    @MockBean(name = "orderDao")
    protected OrderDao orderDao;

    @MockBean(name = "orderLineItemDao")
    protected OrderLineItemDao orderLineItemDao;

    protected Order getOrder() {
        final Order order = new Order();
        order.setOrderStatus(COOKING.name());
        order.setOrderTableId(1L);
        order.setOrderedTime(LocalDateTime.now());
        return order;
    }

    protected OrderLineItem getOrderLineItem(final Long orderId) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setQuantity(1);
        orderLineItem.setMenuId(1L);
        return orderLineItem;
    }

    protected OrderTable getOrderTable() {
        return getOrderTable(null);
    }

    protected OrderTable getOrderTable(final Long tableGroupId) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(1);
        return orderTable;
    }
}
