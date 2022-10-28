package kitchenpos.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderLineItemMapperImpl implements OrderLineItemMapper {

    @Override
    public List<OrderLineItem> toOrderLineItems(final List<OrderLineItemCreateRequest> orderLineItemCreateRequests) {
        return orderLineItemCreateRequests.stream()
                .map(this::createOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem createOrderLineItem(final OrderLineItemCreateRequest orderLineItemCreateRequest) {
        return new OrderLineItem(
                null, null, orderLineItemCreateRequest.getMenuId(), orderLineItemCreateRequest.getQuantity()
        );
    }
}
