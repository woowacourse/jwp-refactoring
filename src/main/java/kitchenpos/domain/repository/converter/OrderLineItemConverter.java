package kitchenpos.domain.repository.converter;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.persistence.dto.OrderLineItemDataDto;
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
