package kitchenpos.event.application;

import java.util.List;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.order.application.OrderStartedEvent;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableDao;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ValidateOrderTableToCreateOrderEventHandler {

    private OrderTableDao orderTableDao;
    private MenuDao menuDao;

    public ValidateOrderTableToCreateOrderEventHandler(OrderTableDao orderTableDao, MenuDao menuDao) {
        this.orderTableDao = orderTableDao;
        this.menuDao = menuDao;
    }

    @EventListener
    public void shouldNotOrderTableEmpty(OrderStartedEvent event) {
        Order order = event.getOrder();
        OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    @EventListener
    public void shouldExistingLineItems(OrderStartedEvent event) {
        List<OrderLineItem> orderLineItems = event.getOrderLineItems();
        List<Long> menuIds = event.getMenuIds();
        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }
}
