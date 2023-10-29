package order.application;

import order.domain.service.OrderValidator;
import table.application.dto.event.OrderTableChangeEmptyEvent;
import table.application.dto.event.TableGroupCreateEvent;
import table.application.dto.event.TableGroupUnGroupEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TableEventListener {

    private final OrderValidator orderValidator;

    public TableEventListener(final OrderValidator orderValidator) {
        this.orderValidator = orderValidator;
    }

    @EventListener
    public void validateToCreateTableGroup(TableGroupCreateEvent event) {
        orderValidator.validateCompletedOrders(event.getOrderTables());
    }

    @EventListener
    public void validateToUnGroupTableGroup(TableGroupUnGroupEvent event) {
        orderValidator.validateCompletedOrders(event.getOrderTables());
    }

    @EventListener
    public void validateToChangeEmpty(OrderTableChangeEmptyEvent event) {
        orderValidator.validateCompletedOrders(event.getOrderTableId());
    }
}
