package kitchenpos.application.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CreateOrderDto {

    private final Long orderTableId;
    private final List<CreateOrderLineItemDto> orderLineItems;

    public CreateOrderDto(Long orderTableId,
                          List<CreateOrderLineItemDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }
}
