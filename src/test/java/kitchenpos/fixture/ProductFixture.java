package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductFixture {

  public static Product createProduct() {
    return new Product("price", BigDecimal.TEN);
  }
}
