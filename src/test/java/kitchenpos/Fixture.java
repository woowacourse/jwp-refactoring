package kitchenpos;

import java.math.BigDecimal;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

public class Fixture {

    public static final Long INVALID_ID = -1L;
    public static final Product PRODUCT = new Product("Product1", BigDecimal.valueOf(10000));
    public static final MenuGroup MENU_GROUP = new MenuGroup("Group1");
    public static final OrderTable ORDER_TABLE_EMPTY = new OrderTable(0, true);
    public static final OrderTable ORDER_TABLE_NOT_EMPTY = new OrderTable(0, false);
}
