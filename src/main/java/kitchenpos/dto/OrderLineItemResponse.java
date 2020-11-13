package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderLineItemResponse {

    private Long seq;
    private Long menuId;
    private long quantity;

    public static List<OrderLineItemResponse> listFrom(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(OrderLineItemResponse::from)
            .collect(Collectors.toList());
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        Menu menu = orderLineItem.getMenu();

        return OrderLineItemResponse.builder()
            .seq(orderLineItem.getSeq())
            .menuId(menu.getId())
            .quantity(orderLineItem.getQuantity())
            .build();
    }
}
