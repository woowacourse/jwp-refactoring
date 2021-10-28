package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

import static kitchenpos.fixture.MenuFixture.*;

public class OrderLineItemFixture {
    public static OrderLineItem 후라이드_단품_둘 = new OrderLineItem(후라이드_단품, 2);
    public static OrderLineItem 양념반_후라이드반_하나 = new OrderLineItem(양념반_후라이드반, 1);
}
