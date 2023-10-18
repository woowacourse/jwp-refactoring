package kitchenpos.fixture;

import kitchenpos.domain.Menu2;
import kitchenpos.domain.MenuProduct2;
import kitchenpos.domain.Product2;

public class MenuProductFixture {

  public static MenuProduct2 createMenuProduct(
      final Product2 product2
  ) {
    return new MenuProduct2(
        product2,
        4L
    );
  }
}
