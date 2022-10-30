package kitchenpos.support;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.order.application.dto.OrderRequestDto;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.presentation.dto.OrderLineItemRequest;

public class OrderFixture {

    public static OrderRequestDto 주문_생성(final Long orderTableId, final MenuResponse menu) {
        return new OrderRequestDto(orderTableId,convertMenuProductToOrderLineItem(menu));
    }

    private static List<OrderLineItemRequest> convertMenuProductToOrderLineItem(MenuResponse menu) {
        return menu.getMenuProducts().stream()
                .map(menuProduct -> new OrderLineItem(menuProduct.getMenuId(), menuProduct.getQuantity()))
                .map(orderLineItem -> new OrderLineItemRequest(orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());
    }
}
