package kitchenpos.order.application;

import static kitchenpos.order.exception.OrderExceptionType.ORDER_STATUS_IS_COOKING_OR_MEAL;

import java.util.List;
import kitchenpos.event.TableUngroupedEvent;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.exception.OrderException;
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
            throw new OrderException(ORDER_STATUS_IS_COOKING_OR_MEAL);
        }
    }
}
