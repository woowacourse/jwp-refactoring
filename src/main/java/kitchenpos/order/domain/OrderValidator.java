package kitchenpos.order.domain;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    public void validate(final Order order) {
        validateEmptyTable(order);
        validateEmptyOrderLineItems(order);
    }

    private void validateEmptyTable(final Order order) {
        if (order.getOrderTable().isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있어 주문을 할 수 없습니다.");
        }
    }

    private void validateEmptyOrderLineItems(final Order order) {
        if (CollectionUtils.isEmpty(order.getOrderLineItems())) {
            throw new IllegalArgumentException("주문 품목이 없어 주문할 수 없습니다.");
        }
    }
}
