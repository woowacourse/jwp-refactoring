package kitchenpos.application.event.listener;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.event.ValidateOrderStatusEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TableEventListener {
    private OrderDao orderDao;

    public TableEventListener(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @EventListener
    public void validateOrderStatusOfTable(ValidateOrderStatusEvent event) {
        OrderTable orderTable = (OrderTable) event.getSource();

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), OrderStatus.getUnmodifiableStatus())) {
            throw new IllegalArgumentException();
        }
    }
}
