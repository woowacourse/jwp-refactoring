package application;

import domain.OrderStatus;
import domain.OrderTable;
import domain.OrderTableChangeEvent;
import domain.OrderTableLogRepository;
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
