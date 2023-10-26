package kitchenpos.support.fixture.dto;

import kitchenpos.order.application.dto.OrderStatusChangeRequest;

public abstract class OrderStatusChangeRequestFixture {

    public static OrderStatusChangeRequest orderStatusChangeRequest(final String orderStatus) {
        return new OrderStatusChangeRequest(orderStatus);
    }
}
