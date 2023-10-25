package kitchenpos.order;

import kitchenpos.menu.MenuDao;
import kitchenpos.ordertable.OrderTableDao;
import kitchenpos.ordertable.OrderTable;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderEventListener {

    private final MenuDao menuDao;
    private final OrderTableDao orderTableDao;

    public OrderEventListener(MenuDao menuDao, OrderTableDao orderTableDao) {
        this.menuDao = menuDao;
        this.orderTableDao = orderTableDao;
    }

    @EventListener
    public void createOrder(Order order) {
        validateMenu(order);
        validateOrderTable(order);
    }

    private void validateMenu(Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTable(Order order) {
        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
