package kitchenpos.order.application;

import static kitchenpos.order.exception.OrderExceptionType.ORDER_STATUS_IS_COOKING_OR_MEAL;

import java.util.List;
import kitchenpos.event.TableEmptyChangedEvent;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.exception.OrderException;
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
