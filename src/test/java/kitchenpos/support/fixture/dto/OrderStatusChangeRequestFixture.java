package kitchenpos.support.fixture.dto;

import kitchenpos.application.dto.order.OrderStatusChangeRequest;

public abstract class OrderStatusChangeRequestFixture {

    public static OrderStatusChangeRequest orderStatusChangeRequest(final String orderStatus) {
        return new OrderStatusChangeRequest(orderStatus);
    }
}
