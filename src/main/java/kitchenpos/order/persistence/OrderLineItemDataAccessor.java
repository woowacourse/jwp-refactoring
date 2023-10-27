package kitchenpos.order.persistence;

import java.util.List;
import kitchenpos.order.persistence.dto.OrderLineItemDataDto;
import kitchenpos.support.BasicDataAccessor;

public interface OrderLineItemDataAccessor extends BasicDataAccessor<OrderLineItemDataDto> {

    List<OrderLineItemDataDto> findAllByOrderId(Long orderId);
}
