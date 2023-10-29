package kitchenpos.global;

import java.math.BigDecimal;
import kitchenpos.menu.dto.request.MenuGroupCreateRequest;
import kitchenpos.menu.dto.request.ProductCreateRequest;
import kitchenpos.table.dto.request.OrderTableCreateRequest;

public class Fixture {

    public static final Long INVALID_ID = -1L;
    public static final ProductCreateRequest PRODUCT = new ProductCreateRequest("Product1", BigDecimal.valueOf(10000));
    public static final MenuGroupCreateRequest MENU_GROUP = new MenuGroupCreateRequest("Group1");
    public static final OrderTableCreateRequest ORDER_TABLE_EMPTY = new OrderTableCreateRequest(0, true);
    public static final OrderTableCreateRequest ORDER_TABLE_NOT_EMPTY = new OrderTableCreateRequest(0, false);
}
