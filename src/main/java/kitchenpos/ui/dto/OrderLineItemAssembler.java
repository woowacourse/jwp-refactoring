package kitchenpos.ui.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemAssembler {

    private OrderLineItemAssembler() {
    }

    public static OrderLineItem assemble(
        OrderLineItemsRequest orderLineItemsRequest, List<Menu> menus) {
        Menu menu = findMenu(menus, orderLineItemsRequest.getMenuId());
        return OrderLineItem.entityOf(menu, orderLineItemsRequest.getQuantity());
    }

    private static Menu findMenu(List<Menu> menus, long menuId) {
        return menus.stream()
            .filter(menu -> menu.getId().equals(menuId))
            .findFirst()
            .orElse(null);
    }

    public static List<OrderLineItem> listAssemble(
        List<OrderLineItemsRequest> orderLineItems, List<Menu> menus) {
        return orderLineItems.stream()
            .map(orderLineItemsOfOrderRequest -> assemble(orderLineItemsOfOrderRequest, menus))
            .collect(Collectors.toList());
    }
}
