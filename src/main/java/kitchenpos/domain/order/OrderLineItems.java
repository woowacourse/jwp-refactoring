package kitchenpos.domain.order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderLineItem> values = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(final List<OrderLineItem> values, final Order order) {
        addAll(values, order);
    }

    private void addAll(final List<OrderLineItem> orderLineItems, final Order order) {
        final List<OrderLineItem> orderInserted = orderLineItems.stream()
                .map(orderLineItem ->
                        new OrderLineItem(
                                order,
                                orderLineItem.getMenuName(),
                                orderLineItem.getMenuPriceValue(),
                                orderLineItem.getMenuId(),
                                orderLineItem.getQuantity()
                        )
                )
                .collect(Collectors.toList());
        values.addAll(orderInserted);
    }

    public List<OrderLineItem> getValues() {
        return values;
    }
}
