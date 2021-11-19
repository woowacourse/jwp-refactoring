package kitchenpos.order.domain;

import org.springframework.stereotype.Component;

import kitchenpos.table.domain.OrderTable;

@Component
public class OrderValidator {

    public void validate(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 주문할 수 없습니다.");
        }
    }
}
