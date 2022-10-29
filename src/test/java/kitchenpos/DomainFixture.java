package kitchenpos;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderLineItem;

public class DomainFixture {

    public static MenuGroup getMenuGroup() {
        return new MenuGroup("마이쮸 1종 세트");
    }

    public static List<OrderLineItem> getOrderLineItems(final Long menuId, final long quantity) {
        return List.of(new OrderLineItem(null, null, menuId, quantity));
    }
}
