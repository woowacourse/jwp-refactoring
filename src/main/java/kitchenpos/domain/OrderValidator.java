package kitchenpos.domain;

import java.util.List;

public interface OrderValidator {

    void validateMenuExists(List<OrderLineItem> orderLineItems, List<Long> menuIds);
}
