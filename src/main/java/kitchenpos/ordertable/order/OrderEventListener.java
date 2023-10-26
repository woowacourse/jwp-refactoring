package kitchenpos.ordertable.order;

import kitchenpos.order.OrderCreatedEvent;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTableDao;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final OrderTableDao orderTableDao;

    public OrderEventListener(OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @EventListener
    public void createOrder(OrderCreatedEvent event) {
        validateOrderTable(event);
    }

    private void validateOrderTable(OrderCreatedEvent event) {
        final OrderTable orderTable = orderTableDao.findById(event.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
