package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;

public class MenuFixture {

    public static MenuRequest generateMenuRequest(final String name,
                                                  final BigDecimal price,
                                                  final MenuGroup menuGroup,
                                                  final List<MenuProductRequest> menuProducts) {
        return new MenuRequest(name, price, menuGroup.getId(), menuProducts);
    }

    public static Menu generateMenu(final String name,
                                    final BigDecimal price,
                                    final MenuGroup menuGroup,
                                    final List<MenuProduct> menuProducts) {
        return new Menu(name, new Price(price), menuGroup.getId(), menuProducts);
    }
}
