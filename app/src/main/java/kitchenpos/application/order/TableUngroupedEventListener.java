package kitchenpos.application.order;

import static kitchenpos.exception.table.TableGroupExceptionType.CAN_NOT_UNGROUP_COOKING_OR_MEAL;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.event.TableUngroupedEvent;
import kitchenpos.exception.table.TableGroupException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TableUngroupedEventListener {

    private final OrderRepository orderRepository;

    public TableUngroupedEventListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void validateAllOrdersAreCompletion(TableUngroupedEvent tableUngroupedEvent) {
        List<Long> orderTableIds = tableUngroupedEvent.orderTableIds();
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(orderTableIds);
        if (orders.stream().anyMatch(Order::isCookingOrMeal)) {
            throw new TableGroupException(CAN_NOT_UNGROUP_COOKING_OR_MEAL);
        }
    }
}
