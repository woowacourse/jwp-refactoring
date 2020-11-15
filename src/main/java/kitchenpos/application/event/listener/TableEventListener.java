package kitchenpos.application.event.listener;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.event.ValidateTableStatusEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TableEventListener {
    private OrderDao orderDao;

    public TableEventListener(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @EventListener
    public void validateStatusChange(ValidateTableStatusEvent event) {
        OrderTable orderTable = (OrderTable) event.getSource();

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
        ) {
            throw new IllegalArgumentException();
        }
    }
}
