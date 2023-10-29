package kitchenpos.ordertable.eventlistener;

import kitchenpos.order.dto.OrderCreatedEvent;
import kitchenpos.ordertable.dao.OrderTableDao;
import kitchenpos.ordertable.domain.OrderTable;
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
