package kitchenpos.ui.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ChangeOrderDto;

public class UpdateOrderResponse {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<UpdateOrderLineItemResponse> orderLineItems;

    public UpdateOrderResponse(final ChangeOrderDto dto) {
        this.id = dto.getId();
        this.orderTableId = dto.getOrderTableId();
        this.orderStatus = dto.getOrderStatus().name();
        this.orderedTime = dto.getOrderedTime();
        this.orderLineItems = convertUpdateOrderLineItems(dto);
    }

    private List<UpdateOrderLineItemResponse> convertUpdateOrderLineItems(final ChangeOrderDto changeOrderDto) {
        return changeOrderDto.getOrderLineItems()
                    .stream()
                    .map(changeOrderLineItemDto ->
                            new UpdateOrderLineItemResponse(changeOrderDto, changeOrderLineItemDto)
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

    public List<UpdateOrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
