package kitchenpos;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.domain.MenuProduct;

public class RequestFixture {

    public static final MenuProduct MENU_PRODUCT_REQUEST = new MenuProduct(1L, 1L,
            1L, 2L);

    public static final MenuCreateRequest MENU_CREATE_REQUEST
            = new MenuCreateRequest("후라이드", BigDecimal.valueOf(19000), 1L, List.of(MENU_PRODUCT_REQUEST));
}
