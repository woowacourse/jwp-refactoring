package kitchenpos.fixture;

import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.request.OrderUpdateStatusRequest;

public class OrderFixture extends DomainCreator {

    public static OrderUpdateStatusRequest createRequestOrderStatus(final OrderStatus orderStatus) {
        return new OrderUpdateStatusRequest(orderStatus.name());
    }
}
