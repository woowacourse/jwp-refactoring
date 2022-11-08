package kitchenpos.core.table.application;

import java.util.NoSuchElementException;
import kitchenpos.core.order.domain.OrderChangeEvent;
import kitchenpos.core.order.domain.OrderStatus;
import kitchenpos.core.table.domain.OrderTable;
import kitchenpos.core.table.domain.OrderTableDao;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderChangeEventHandler {

    private final OrderTableDao orderTableDao;

    public OrderChangeEventHandler(final OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @TransactionalEventListener
    public void changeOrderStatus(final OrderChangeEvent event) {
        final OrderTable orderTable = getOrderTable(event);
        orderTable.changeOrderStatus(OrderStatus.from(event.getOrderStatus()));
        orderTableDao.save(orderTable);
    }

    private OrderTable getOrderTable(final OrderChangeEvent event) {
        return orderTableDao.findById(event.getOrderTableId())
                .orElseThrow(NoSuchElementException::new);
    }
}
