package kitchenpos.order.application;

import kitchenpos.order.domain.OrderTable;
import org.springframework.stereotype.Service;

@Service
public class OrderValidator {

    public void validate(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
