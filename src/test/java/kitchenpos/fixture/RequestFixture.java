package kitchenpos.fixture;

import static java.util.Collections.*;

import java.math.BigDecimal;

import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.application.dto.ProductRequest;
import kitchenpos.domain.MenuProduct;

public class RequestFixture {
    public static final MenuGroupRequest MENU_GROUP_REQUEST = new MenuGroupRequest("추천메뉴");
    public static final MenuRequest MENU_REQUEST = new MenuRequest("후라이드+후라이드",
            BigDecimal.valueOf(19_000L), 1L, singletonList(new MenuProduct(null, null, 1L, 2L)));
    public static final ProductRequest PRODUCT_REQUEST = new ProductRequest("강정치킨",
            BigDecimal.valueOf(17_000L));
    public static final OrderTableRequest ORDER_TABLE_REQUEST = new OrderTableRequest(0, true);
}
