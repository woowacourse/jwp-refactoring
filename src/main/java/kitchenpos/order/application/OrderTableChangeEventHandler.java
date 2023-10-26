package kitchenpos.order.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTableLogRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderTableChangeEventHandler {

    private final OrderTableLogRepository orderTableLogRepository;

    public OrderTableChangeEventHandler(OrderTableLogRepository orderTableLogRepository) {
        this.orderTableLogRepository = orderTableLogRepository;
    }

    @EventListener
    public void checkOrderStatusIsCompletion(OrderTableChangeEvent event) {
        OrderTable orderTable = event.getOrderTable();
        orderTableLogRepository.findAllByOrderTableId(orderTable.getId())
                .stream()
                .filter(tableLog -> !OrderStatus.checkWhetherCompletion(tableLog.getOrder().getOrderStatus()))
                .findFirst()
                .ifPresent(tableLog -> tableLog.getOrder().validateOrderStatusIsCompletion());
    }
}
