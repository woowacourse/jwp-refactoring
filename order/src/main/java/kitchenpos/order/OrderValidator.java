package kitchenpos.order;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderValidator {

    public void validateOrderLineItemExist(final Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 존재하지 않습니다. 주문을 등록할 수 없습니다.");
        }
    }
}
