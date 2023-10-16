package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuFixture {

  public static Menu 만냥치킨_2마리(final BigDecimal price, final Long menuGroupId) {
    final MenuProduct menuProduct = new MenuProduct(null, null, 1L, 2L);

    return new Menu("만냥치킨+만냥치킨", price, menuGroupId, List.of(menuProduct));
  }

  public static Menu 만냥치킨_2마리_잘못된_상품() {
    final MenuProduct menuProduct = new MenuProduct(null, null, 999L, 2L);

    return new Menu("만냥치킨+만냥치킨", BigDecimal.valueOf(19000), 1L, List.of(menuProduct));
  }

}
