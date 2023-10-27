package kitchenpos.application.order;

import static kitchenpos.exception.order.OrderExceptionType.ORDER_STATUS_IS_COOKING_OR_MEAL;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.event.TableEmptyChangedEvent;
import kitchenpos.exception.order.OrderException;
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
            throw new OrderException(ORDER_STATUS_IS_COOKING_OR_MEAL);
        }
    }
}
