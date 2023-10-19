package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class MenuProducts {

  private final List<MenuProduct> menuProducts;

  public MenuProducts(final List<MenuProduct> menuProducts) {
    this.menuProducts = menuProducts;
  }

  public boolean isSumLowerThan(final BigDecimal price) {
    return price.compareTo(sum()) > 0;
  }

  private BigDecimal sum() {
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
