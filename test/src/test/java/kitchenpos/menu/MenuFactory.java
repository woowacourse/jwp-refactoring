package kitchenpos.menu;

import domain.Menu;
import domain.MenuGroup;
import domain.MenuProduct;
import java.math.BigDecimal;
import java.util.List;
import ui.request.MenuCreateRequest;
import ui.request.MenuProductCreateRequest;

public final class MenuFactory {

    private MenuFactory() {
    }

    public static Menu createMenuOf(
            final String name,
            final BigDecimal price,
            final MenuGroup menuGroup,
            final MenuProduct... menuProducts
    ) {
        return new Menu(name, price, List.of(menuProducts), menuGroup.getId());
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
