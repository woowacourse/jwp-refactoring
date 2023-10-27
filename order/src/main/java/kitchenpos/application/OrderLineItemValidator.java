package kitchenpos.application;

import kitchenpos.application.dto.request.OrderLineItemRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderLineItemValidator {

    public void validate(List<OrderLineItemRequest> orderLineItemRequests, long orderMenuSize) {
        if (orderLineItemRequests.size() < 1) {
            throw new IllegalArgumentException();
        }

        if (orderMenuSize != orderLineItemRequests.size()) {
            throw new IllegalArgumentException();
        }
    }
}
