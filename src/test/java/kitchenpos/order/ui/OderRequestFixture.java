package kitchenpos.order.ui;

import java.util.List;
import kitchenpos.order.application.dto.request.OrderCreateRequest;
import kitchenpos.order.application.dto.request.OrderLineItemRequest;
import kitchenpos.order.application.dto.request.OrderStatusChangeRequest;

public class OderRequestFixture {

    // 주문
    public static OrderCreateRequest orderCreateRequest() {
        return new OrderCreateRequest(
                1,
                List.of(new OrderLineItemRequest(1, 1))
        );
    }

    public static OrderStatusChangeRequest orderStatusChangeRequest_MEAL() {
        return new OrderStatusChangeRequest("MEAL");
    }

    public static OrderStatusChangeRequest orderStatusChangeRequest_COMPLETION() {
        return new OrderStatusChangeRequest("COMPLETION");
    }
}
