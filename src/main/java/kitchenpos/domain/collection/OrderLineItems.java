package kitchenpos.domain.collection;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.entity.Menu;
import kitchenpos.domain.entity.Order;
import kitchenpos.domain.entity.OrderLineItem;

public class OrderLineItems {

    private List<OrderLineItem> elements;

    public OrderLineItems(List<OrderLineItem> elements) {
        this.elements = elements;
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public List<Long> getMenuIds() {
        return elements.stream()
                .map(OrderLineItem::getMenu)
                .map(Menu::getId)
                .collect(Collectors.toList());
    }

    public int numberOfElements() {
        return elements.size();
    }

    public void associateOrder(Order order) {
        elements.forEach(element -> element.associateOrder(order));
    }

    public List<OrderLineItem> getElements() {
        return elements;
    }
}
