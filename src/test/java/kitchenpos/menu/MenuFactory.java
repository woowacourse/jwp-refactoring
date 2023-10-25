package kitchenpos.menu;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.ui.request.MenuCreateRequest;
import kitchenpos.menu.ui.request.MenuProductCreateRequest;
import kitchenpos.menugroup.domain.MenuGroup;

public final class MenuFactory {

    private MenuFactory() {
    }

    public static Menu createMenuOf(
            final String name,
            final BigDecimal price,
            final MenuGroup menuGroup,
            final MenuProduct... menuProducts
    ) {
        return new Menu(name, price, List.of(menuProducts), menuGroup);
    }

    public static MenuCreateRequest createMenuRequestOf(
            final String name,
            final BigDecimal price,
            final Long menuGroupId,
            final MenuProductCreateRequest... menuProductCreateRequests
    ) {
        return new MenuCreateRequest(name, price, menuGroupId, List.of(menuProductCreateRequests));
    }
}
