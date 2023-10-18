package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Menu2;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.domain.Product2;

public class MenuFixture {

  public static Menu2 createMenu(final MenuGroup menuGroup, final Product2 product) {
    return new Menu2(
        null,
        "menu",
        BigDecimal.TEN,
        menuGroup,
        List.of(MenuProductFixture.createMenuProduct(product))
    );
  }

  public static Menu2 createExceedPriceMenu(final MenuGroup menuGroup, final Product2 product) {
    return new Menu2(
        null,
        "menu",
        BigDecimal.valueOf(100000000),
        menuGroup,
        List.of(MenuProductFixture.createMenuProduct(product))
    );
  }
}
