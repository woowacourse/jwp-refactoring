package kitchenpos.ui.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateOrderDto;

public class CreateOrderResponse {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<CreateOrderLineItemResponse> orderLineItems;

    public CreateOrderResponse(final CreateOrderDto dto) {
        this.id = dto.getId();
        this.orderTableId = dto.getOrderTableId();
        this.orderStatus = dto.getOrderStatus().name();
        this.orderedTime = dto.getOrderedTime();
        this.orderLineItems = convertCreateOrderLineItems(dto);
    }

    private List<CreateOrderLineItemResponse> convertCreateOrderLineItems(final CreateOrderDto createOrderDto) {
        return createOrderDto.getOrderLineItems()
                             .stream()
                             .map(createOrderLineItemDto ->
                                     new CreateOrderLineItemResponse(createOrderDto, createOrderLineItemDto)
                             )
                             .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<CreateOrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
