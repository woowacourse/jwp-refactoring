package kitchenpos.application.fixture;

import kitchenpos.domain.common.Price;
import kitchenpos.domain.order.OrderMenu;

import java.math.BigDecimal;

public abstract class OrderMenuFixture {

    private OrderMenuFixture() {
    }

    public static OrderMenu orderMenu(final Long menuId, final String name, final BigDecimal price) {
        return new OrderMenu(menuId, name, new Price(price));
    }
}
