package com.kitchenpos.event.message;

import java.util.List;

public class ValidatorHavingMeal {

    private final List<Long> orderIds;

    public ValidatorHavingMeal(final List<Long> orderIds) {
        this.orderIds = orderIds;
    }

    public List<Long> getOrderIds() {
        return orderIds;
    }
}
