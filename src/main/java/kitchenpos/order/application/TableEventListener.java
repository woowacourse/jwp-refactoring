package kitchenpos.order.application;

import kitchenpos.order.domain.service.OrderValidator;
import kitchenpos.table.application.dto.event.OrderTableChangeEmptyEvent;
import kitchenpos.table.application.dto.event.TableGroupCreateEvent;
import kitchenpos.table.application.dto.event.TableGroupUnGroupEvent;
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
    public void validate(OrderTableChangeEmptyEvent event) {
        orderValidator.validateCompletedOrders(event.getOrderTableId());
    }
}
