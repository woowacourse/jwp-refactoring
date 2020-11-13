package kitchenpos.ui.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import kitchenpos.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderResponse {
    @NotNull
    private final Long id;

    @NotNull
    private final Long orderTableId;

    @NotBlank
    private final String orderStatus;

    @NotNull
    private final LocalDateTime orderedTime;

    @NotEmpty
    @Valid
    private final List<OrderLineItemResponse> orderLineItems;

    public static OrderResponse from(final Order order) {
        final List<OrderLineItemResponse> orderLineItemResponses = order.getOrderLineItems()
            .stream()
            .map(OrderLineItemResponse::from)
            .collect(Collectors.toList());

        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus()
            , order.getOrderedTime(), orderLineItemResponses);
    }
}
