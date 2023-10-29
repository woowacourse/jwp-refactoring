package kitchenpos.order.stub;

import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderValidator;

public class OrderValidatorStub implements OrderValidator {

    @Override
    public void validatePrepare(final Long orderTableId, final OrderLineItems orderLineItems) {
        /** NOT IMPLEMENTED **/
    }
}
