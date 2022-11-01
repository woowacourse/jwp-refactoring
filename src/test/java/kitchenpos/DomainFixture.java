package kitchenpos;

import java.util.List;
import kitchenpos.domain.OrderLineItem;

public class DomainFixture {

    public static List<OrderLineItem> getOrderLineItems(final Long menuId, final long quantity) {
        return List.of(new OrderLineItem(null, null, menuId, quantity));
    }
}
