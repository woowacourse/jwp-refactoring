package kitchenpos.support.fixture.dto;

import kitchenpos.ui.dto.order.OrderStatusChangeRequest;

public class OrderStatusChangeRequestFixture {

    public static OrderStatusChangeRequest orderStatusChangeRequest(final String orderStatus) {
        return new OrderStatusChangeRequest(orderStatus);
    }
}
