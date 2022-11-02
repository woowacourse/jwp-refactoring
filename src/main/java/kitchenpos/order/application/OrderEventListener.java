package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

import java.util.List;
import kitchenpos.order.domain.dao.OrderDao;
import kitchenpos.order.exception.InvalidChangedEmptyRequest;
import kitchenpos.table.event.ChangedEmptyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final OrderDao orderDao;

    public OrderEventListener(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @EventListener
    public void validateChangeEmpty(ChangedEmptyEvent changedEmptyEvent) {
        if (shouldNotChangeEmpty(changedEmptyEvent.getOrderTableId())) {
            throw new InvalidChangedEmptyRequest();
        }
    }

    private boolean shouldNotChangeEmpty(Long orderTableId) {
        return orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of(COOKING.name(), MEAL.name()));
    }
}
