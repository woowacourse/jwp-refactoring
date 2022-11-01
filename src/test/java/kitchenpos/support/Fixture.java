package kitchenpos.support;

import java.math.BigDecimal;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

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
