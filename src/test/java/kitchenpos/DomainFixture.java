package kitchenpos;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

public class DomainFixture {

    public static Product getProduct() {
        return new Product("마이쮸", BigDecimal.valueOf(800));
    }

    public static OrderTable getEmptyTable() {
        return new OrderTable(0, true);
    }

    public static OrderTable getNotEmptyTable(final int numberOfGuests) {
        return new OrderTable(numberOfGuests, false);
    }

    public static MenuGroup getMenuGroup() {
        return new MenuGroup("마이쮸 1종 세트");
    }

    public static Order getOrder(final Long orderTableId, final Long menuId) {
        return new Order(
                orderTableId,
                getOrderLineItems(menuId, 1));
    }

    public static List<OrderLineItem> getOrderLineItems(final Long menuId, final long quantity) {
        return List.of(new OrderLineItem(null, null, menuId, quantity));
    }
}
