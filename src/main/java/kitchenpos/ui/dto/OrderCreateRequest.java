package kitchenpos.ui.dto;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import kitchenpos.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(onConstructor_ = @ConstructorProperties("name"))
@Getter
public class OrderCreateRequest {
    @NotNull
    private final Long orderTableId;

    @NotEmpty
    @Valid
    private final List<OrderLineItemRequest> orderLineItems;

    public Order toRequestEntity() {
        return Order.builder()
            .orderTableId(orderTableId)
            .orderLineItems(orderLineItems.stream()
                .map(OrderLineItemRequest::toRequestEntity)
                .collect(Collectors.toList())
            ).build();
    }
}
