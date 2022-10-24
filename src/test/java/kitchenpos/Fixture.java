package kitchenpos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Product;

public class Fixture {

    private static final OrderLineItem ORDER_LINE_ITEM = new OrderLineItem(1L, 1L);
    public static final Order ORDER =  new Order(1L,
            OrderStatus.COOKING.name(), LocalDateTime.now(), List.of(ORDER_LINE_ITEM));

    public static final MenuProduct MENU_PRODUCT = new MenuProduct(1L, 1L, 1L, 2L);
    public static final Menu MENU =  new Menu(1L, "후라이드+후라이드",
            BigDecimal.valueOf(19000), 1L, List.of(MENU_PRODUCT));

    public static final Product PRODUCT = new Product("후라이드 치킨",  BigDecimal.valueOf(10000));
}
