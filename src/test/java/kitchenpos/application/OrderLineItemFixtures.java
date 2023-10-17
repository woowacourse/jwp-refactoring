package kitchenpos.application;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.Product;

public class OrderLineItemFixtures {

    public static OrderLineItem 로제떡볶이_주문항목() {
        Product 로제떡볶이 = ProductFixtures.로제떡볶이();
        return new OrderLineItem(1L, null, 로제떡볶이.getId(), 1);
    }
}
