package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderLineItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemResponse {

    private Long seq;
    private MenuResponse menu;
    private long quantity;

    public static List<OrderLineItemResponse> listFrom(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(OrderLineItemResponse::from)
            .collect(Collectors.toList());
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        MenuResponse menu = MenuResponse.from(orderLineItem.getMenu());

        return OrderLineItemResponse.builder()
            .seq(orderLineItem.getSeq())
            .menu(menu)
            .quantity(orderLineItem.getQuantity())
            .build();
    }
}
