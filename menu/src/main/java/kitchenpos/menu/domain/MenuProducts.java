package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.domain.Product;

public class MenuProducts {

  private final List<MenuProduct> menuProducts;

  public MenuProducts(final List<MenuProduct> menuProducts) {
    this.menuProducts = menuProducts;
  }

  public void validateSumLowerThan(final BigDecimal price) {
    if (price.compareTo(sumProductsPrice()) > 0) {
      throw new IllegalArgumentException();
    }
  }

  private BigDecimal sumProductsPrice() {
    BigDecimal sum = BigDecimal.ZERO;

    for (final MenuProduct menuProduct : menuProducts) {
      final Product product = menuProduct.getProduct();
      sum = sum.add(product.getPrice()
          .multiply(BigDecimal.valueOf(menuProduct.getQuantity()))
      );
    }

    return sum;
  }
}
