package kitchenpos.persistence.specific;

import java.util.List;
import kitchenpos.persistence.BasicDataAccessor;
import kitchenpos.persistence.dto.OrderLineItemDataDto;

public interface OrderLineItemDataAccessor extends BasicDataAccessor<OrderLineItemDataDto> {

    List<OrderLineItemDataDto> findAllByOrderId(Long orderId);
}
