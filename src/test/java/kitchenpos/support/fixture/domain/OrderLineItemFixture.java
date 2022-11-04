package kitchenpos.support.fixture.domain;

import java.math.BigDecimal;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

public enum OrderLineItemFixture {

    ONE(1L, "치킨", new BigDecimal(1000)),
    TWO(2L, "피자", new BigDecimal(2000))
    ;

    private final Long quantity;
    private final String name;
    private final BigDecimal price;

    OrderLineItemFixture(Long quantity, String name, BigDecimal price) {
        this.quantity = quantity;
        this.name = name;
        this.price = price;
    }

    public OrderLineItem getOrderLineItem(Long menuId, Long orderId) {
        return new OrderLineItem(orderId, new OrderMenu(menuId, Name.of(name), Price.from(price)), quantity);
    }

    public OrderLineItem getOrderLineItem(Long id, Long orderId, Long menuId) {
        return new OrderLineItem(id, orderId, new OrderMenu(menuId, Name.of(name), Price.from(price)), quantity);
    }
}
