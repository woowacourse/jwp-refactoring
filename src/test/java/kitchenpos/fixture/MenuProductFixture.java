package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixture {

  public static MenuProduct createMenuProduct(
      final Product product
  ) {
    return new MenuProduct(
        product,
        4L
    );
  }
}
