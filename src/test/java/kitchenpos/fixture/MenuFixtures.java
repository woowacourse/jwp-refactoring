package kitchenpos.fixture;

import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductCreateRequest;

import java.math.BigDecimal;
import java.util.List;

public class MenuFixtures {

    public static MenuCreateRequest createMenu(final String name, final BigDecimal price, final Long menuGroupId) {
        return new MenuCreateRequest(name, price, menuGroupId);
    }

    public static MenuCreateRequest createMenu(final String name, final BigDecimal price, final Long menuGroupId
            , final List<MenuProductCreateRequest> menuProducts) {
        return new MenuCreateRequest(name, price, menuGroupId, menuProducts);
    }

    public static MenuCreateRequest 후라이드치킨(final Long menuGroupId) {
        return createMenu("후라이드치킨", BigDecimal.valueOf(16000), menuGroupId);
    }

    public static MenuCreateRequest 후라이드치킨(final Long menuGroupId, final List<MenuProductCreateRequest> menuProducts) {
        return createMenu("후라이드치킨", BigDecimal.valueOf(16000), menuGroupId, menuProducts);
    }

    public static MenuCreateRequest 양념치킨(final Long menuGroupId) {
        return createMenu("양념치킨", BigDecimal.valueOf(16000), menuGroupId);
    }
}
