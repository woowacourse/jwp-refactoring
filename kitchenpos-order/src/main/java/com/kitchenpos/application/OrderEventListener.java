package com.kitchenpos.application;

import com.kitchenpos.event.message.ValidatorHavingMeal;
import com.kitchenpos.exception.OrderStatusProgressMealException;
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
