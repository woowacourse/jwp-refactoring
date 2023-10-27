package kitchenpos.ui.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ReadOrderDto;

public class ReadOrderResponse {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<ReadOrderLineItemResponse> orderLineItems;

    public ReadOrderResponse(final ReadOrderDto dto) {
        this.id = dto.getId();
        this.orderTableId = dto.getOrderTableId();
        this.orderStatus = dto.getOrderStatus().name();
        this.orderedTime = dto.getOrderedTime();
        this.orderLineItems = convertReadOrderLineItems(dto);
    }

    private List<ReadOrderLineItemResponse> convertReadOrderLineItems(final ReadOrderDto readOrderDto) {
        return readOrderDto.getOrderLineItems()
                           .stream()
                           .map(readOrderLineItemDto ->
                                   new ReadOrderLineItemResponse(readOrderDto, readOrderLineItemDto)
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

    public List<ReadOrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
