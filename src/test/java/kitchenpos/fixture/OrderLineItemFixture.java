package kitchenpos.fixture;

import kitchenpos.order.domain.OrderLineItem;

import static kitchenpos.fixture.MenuFixture.양념반_후라이드반;
import static kitchenpos.fixture.MenuFixture.후라이드_단품;

public class OrderLineItemFixture {
    public static OrderLineItem 후라이드_단품_둘 = new OrderLineItem(후라이드_단품, 2);
    public static OrderLineItem 양념반_후라이드반_하나 = new OrderLineItem(양념반_후라이드반, 1);
}
