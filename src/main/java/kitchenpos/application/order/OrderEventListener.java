package kitchenpos.application.order;

import static kitchenpos.domain.common.OrderStatus.COOKING;
import static kitchenpos.domain.common.OrderStatus.MEAL;

import java.util.List;
import kitchenpos.application.table.UngroupEvent;
import kitchenpos.domain.common.OrderStatus;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.exception.badrequest.CookingOrMealOrderTableCannotUngroupedException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderEventListener {

    private static final List<OrderStatus> NOT_COMPLETED_STATUS = List.of(COOKING, MEAL);

    private final OrderRepository orderRepository;

    public OrderEventListener(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener(UngroupEvent.class)
    public void validateOrdersCompleted(final UngroupEvent event) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(event.getOrderTableIds(), NOT_COMPLETED_STATUS)) {
            throw new CookingOrMealOrderTableCannotUngroupedException();
        }
    }
}
