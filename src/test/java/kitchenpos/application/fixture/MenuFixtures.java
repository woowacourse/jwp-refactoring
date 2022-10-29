package kitchenpos.application.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.MenuSaveRequest;

public class MenuFixtures {

    public static final Menu generateMenu(final String name, final BigDecimal price, final Long menuGroupId) {
        return generateMenu(null, name, price, menuGroupId, List.of());
    }

    public static final Menu generateMenu(final String name,
                                          final BigDecimal price,
                                          final Long menuGroupId,
                                          final List<MenuProduct> menuProducts) {
        return generateMenu(null, name, price, menuGroupId, menuProducts);
    }

    public static final Menu generateMenu(final Long id, final Menu menu) {
        return generateMenu(id, menu.getName(), menu.getPrice(), menu.getMenuGroupId(), menu.getMenuProducts());
    }

    public static final Menu generateMenu(final Long id,
                                          final String name,
                                          final BigDecimal price,
                                          final Long menuGroupId,
                                          final List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public static final MenuSaveRequest generateMenuSaveRequest(final String name,
                                                                final BigDecimal price,
                                                                final Long menuGroupId,
                                                                final List<MenuProduct> menuProducts) {
        return new MenuSaveRequest(name, price, menuGroupId, menuProducts);
    }
}
