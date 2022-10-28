package kitchenpos.dto.mapper;

import java.util.List;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.request.OrderLineItemCreateRequest;

public interface OrderLineItemMapper {

    List<OrderLineItem> toOrderLineItems(List<OrderLineItemCreateRequest> orderLineItemCreateRequests);
}
