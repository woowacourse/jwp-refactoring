package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuProductCreateRequest;

public class MenuFixture {
    public static MenuCreateRequest createMenuRequest(final String menuName, final BigDecimal price, final Long menuGroupId,
                                               final List<MenuProductCreateRequest>menuProductsRequest) {
        return new MenuCreateRequest(
                menuName,
                price,
                menuGroupId,
                menuProductsRequest
        );
    }

    public static MenuCreateRequest createMenuRequest(final Long menuGroupId) {
        return new MenuCreateRequest("맛있는메뉴", BigDecimal.valueOf(20_000L), menuGroupId,
                Collections.singletonList(new MenuProductCreateRequest(1L,1L)));
    }
}
