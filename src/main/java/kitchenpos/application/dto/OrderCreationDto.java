package kitchenpos.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.ui.dto.request.OrderCreationRequest;
import kitchenpos.ui.dto.request.OrderLineItemReeust;

public class OrderCreationDto {

    private final Long orderTableId;
    private final List<OrderLineItemCreationDto> orderLineItems;

    private OrderCreationDto(final Long orderTableId,
                            final List<OrderLineItemCreationDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public static OrderCreationDto from(final OrderCreationRequest orderCreationRequest) {
        final List<OrderLineItemCreationDto> orderLineItemCreationDtos = orderCreationRequest.getOrderLineItems()
                .stream()
                .map(OrderLineItemCreationDto::from)
                .collect(Collectors.toList());

        return new OrderCreationDto(orderCreationRequest.getOrderTableId(), orderLineItemCreationDtos);
    }

    public static Order toEntity(final OrderCreationDto orderCreationDto) {
        final List<OrderLineItem> orderLineItems = orderCreationDto.orderLineItems
                .stream()
                .map(OrderLineItemCreationDto::toEntity)
                .collect(Collectors.toList());

        return new Order(orderCreationDto.getOrderTableId(), orderLineItems);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreationDto> getOrderLineItems() {
        return orderLineItems;
    }
}
