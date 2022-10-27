package kitchenpos.support;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderFixture {

    public static Order 주문_생성(final Long orderTableId, final Menu menu, final String orderStatus) {
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderLineItems(convertMenuProductToOrderLineItem(menu));
        return order;
    }

    private static List<OrderLineItem> convertMenuProductToOrderLineItem(Menu menu) {
        return menu.getMenuProducts().stream()
                .map(menuProduct -> {
                    final OrderLineItem orderLineItem = new OrderLineItem();
                    orderLineItem.setMenuId(menuProduct.getMenuId());
                    orderLineItem.setQuantity(menuProduct.getQuantity());
                    return orderLineItem;
                })
                .collect(Collectors.toList());
    }
}
