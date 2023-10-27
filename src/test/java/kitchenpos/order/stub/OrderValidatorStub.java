package kitchenpos.order.stub;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;

public class OrderValidatorStub extends OrderValidator {

    public OrderValidatorStub() {
        super(null, null);
    }

    @Override
    public void validatePrepare(final Order order) {
        order.changeOrderStatus(OrderStatus.COOKING);
    }
}
