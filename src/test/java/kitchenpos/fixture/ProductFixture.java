package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductFixture {

  public static Product 깐풍치킨(final BigDecimal price) {
    return new Product("깐풍 치킨", price);
  }
}
