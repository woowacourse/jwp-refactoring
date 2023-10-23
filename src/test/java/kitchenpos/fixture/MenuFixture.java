package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;

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
