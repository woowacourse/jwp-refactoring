package kitchenpos.common.event.listener;

import kitchenpos.common.event.message.ValidatorHavingMeal;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.exception.OrderStatusProgressMealException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final OrderService orderService;

    public OrderEventListener(final OrderService orderService) {
        this.orderService = orderService;
    }

    @EventListener
    private void validateOrderStatus(final ValidatorHavingMeal validatorHavingMeal) {
        if (orderService.isStatusInProgressMealByIds(validatorHavingMeal.getOrderIds())) {
            throw new OrderStatusProgressMealException();
        }
    }
}
