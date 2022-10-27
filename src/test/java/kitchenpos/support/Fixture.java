package kitchenpos.support;

import java.math.BigDecimal;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.Product;

public class Fixture {

    public static Product createProduct() {
        return new Product(1L, "productA", BigDecimal.valueOf(1000L));
    }

    public static OrderLineItem createOrderLineItem() {
        return new OrderLineItem(1L, 1L, 1L, 1L);
    }
}
