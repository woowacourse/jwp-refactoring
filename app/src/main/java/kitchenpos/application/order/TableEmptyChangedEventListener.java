package kitchenpos.application.order;

import static kitchenpos.exception.table.OrderTableExceptionType.CAN_NOT_CHANGE_EMPTY_COOKING_OR_MEAL;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.event.TableEmptyChangedEvent;
import kitchenpos.exception.table.OrderTableException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TableEmptyChangedEventListener {

    private final OrderRepository orderRepository;

    public TableEmptyChangedEventListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    public void validateAllOrdersAreCompletion(TableEmptyChangedEvent tableEmptyChangedEvent) {
        List<Order> orders = orderRepository.findAllByOrderTableId(tableEmptyChangedEvent.orderTableId());
        if (orders.stream().anyMatch(Order::isCookingOrMeal)) {
            throw new OrderTableException(CAN_NOT_CHANGE_EMPTY_COOKING_OR_MEAL);
        }
    }
}
