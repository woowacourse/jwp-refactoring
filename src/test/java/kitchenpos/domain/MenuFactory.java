package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menuproduct.MenuProduct;
import kitchenpos.ui.request.MenuCreateRequest;
import kitchenpos.ui.request.MenuProductCreateRequest;

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
