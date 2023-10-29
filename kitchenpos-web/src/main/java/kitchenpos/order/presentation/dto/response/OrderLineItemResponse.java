package kitchenpos.order.presentation.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.vo.MenuInfo;

public class OrderLineItemResponse {

    private final Long id;

    private final MenuInfo menu;

    private final long quantity;

    public OrderLineItemResponse(final Long id,
                                 final MenuInfo menu,
                                 final long quantity) {
        this.id = id;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getId(),
                                         orderLineItem.getMenuInfo(),
                                         orderLineItem.getQuantity());
    }

    public static List<OrderLineItemResponse> convertToList(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                             .map(OrderLineItemResponse::from)
                             .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public MenuInfo getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
