package kitchenpos.order.domain.repository.converter;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.persistence.dto.OrderLineItemDataDto;
import kitchenpos.support.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderLineItemConverter implements Converter<OrderLineItem, OrderLineItemDataDto> {

    @Override
    public OrderLineItemDataDto entityToData(final OrderLineItem orderLineItem) {
        return new OrderLineItemDataDto(
                orderLineItem.getSeq(),
                orderLineItem.getOrderId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity()
        );
    }

    @Override
    public OrderLineItem dataToEntity(final OrderLineItemDataDto orderLineItemDataDto) {
        return new OrderLineItem(
                orderLineItemDataDto.getOrderId(),
                orderLineItemDataDto.getOrderId(),
                orderLineItemDataDto.getMenuId(),
                orderLineItemDataDto.getQuantity()
        );
    }
}
