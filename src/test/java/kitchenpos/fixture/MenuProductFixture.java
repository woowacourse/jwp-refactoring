package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

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
