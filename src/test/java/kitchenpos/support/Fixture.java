package kitchenpos.support;

import java.math.BigDecimal;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.domain.Product;

public class Fixture {

    public static Product createProduct() {
        return new Product(1L, "productA", BigDecimal.valueOf(1000L));
    }

    public static OrderLineItem createOrderLineItem() {
        return new OrderLineItem(1L, new Order(null, null, null, null, null), 1L, 1L);
    }

    public static OrderTable createOrderTableWithNumberOfGuests(int numberOfGuests) {
        return new OrderTable(1L, null, numberOfGuests, false);
    }

    public static OrderTable createEmptyOrderTable() {
        return new OrderTable(1L, null, 0, true);
    }

    public static OrderTable createGroupedOrderTable() {
        return new OrderTable(1L, 1L, 0, true);
    }
}
