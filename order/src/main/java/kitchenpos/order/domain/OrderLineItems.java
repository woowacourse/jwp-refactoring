package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.util.CollectionUtils;


public class OrderLineItems {

    @MappedCollection(idColumn = "ORDER_ID", keyColumn = "ORDER_KEY")
    private final List<OrderLineItem> orderLineItems;

    private OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = new ArrayList<>(orderLineItems);
    }

    public static OrderLineItems of(List<OrderLineItem> orderLineItems, List<Menu> menus) {
        validateEmpty(orderLineItems);
        validateExistItem(orderLineItems, menus);

        return new OrderLineItems(orderLineItems);
    }

    private static void validateEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("하나 이상의 상품을 주문하셔야 합니다.");
        }
    }

    private static void validateExistItem(List<OrderLineItem> orderLineItems, List<Menu> menus) {
        Set<Long> orderLineItemIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .map(AggregateReference::getId)
                .collect(Collectors.toSet());

        Set<Long> menuIds = menus.stream()
                .map(Menu::getId)
                .collect(Collectors.toSet());

        if (!menuIds.containsAll(orderLineItemIds)) {
            throw new IllegalArgumentException("등록되지 않은 상품은 주문할 수 없습니다.");
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
