package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.domain.Product;

public class MenuFixture {

  public static Menu createMenu(final MenuGroup menuGroup, final Product product) {
    return new Menu(
        null,
        "menu",
        BigDecimal.TEN,
        menuGroup,
        List.of(MenuProductFixture.createMenuProduct(product))
    );
  }

  public static Menu createExceedPriceMenu(final MenuGroup menuGroup, final Product product) {
    return new Menu(
        null,
        "menu",
        BigDecimal.valueOf(100000000),
        menuGroup,
        List.of(MenuProductFixture.createMenuProduct(product))
    );
  }
}
