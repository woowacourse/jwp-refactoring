package kitchenpos.ui.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemAssembler {

    private OrderLineItemAssembler() {
    }

    public static OrderLineItem assemble(
        OrderLineItemsOfOrderRequest orderLineItemsOfOrderRequest, List<Menu> menus) {
        Menu menu = findMenu(menus, orderLineItemsOfOrderRequest.getMenuId());
        return OrderLineItem.entityOf(menu, orderLineItemsOfOrderRequest.getQuantity());
    }

    private static Menu findMenu(List<Menu> menus, long menuId) {
        return menus.stream()
            .filter(menu -> menu.getId().equals(menuId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Menu ID에 해당하는 Menu가 없습니다."));
    }

    public static List<OrderLineItem> listAssemble(
        List<OrderLineItemsOfOrderRequest> orderLineItems, List<Menu> menus) {
        return orderLineItems.stream()
            .map(orderLineItemsOfOrderRequest -> assemble(orderLineItemsOfOrderRequest, menus))
            .collect(Collectors.toList());
    }
}
