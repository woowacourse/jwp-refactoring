package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

  public static Product createProduct() {
    return new Product("price", BigDecimal.TEN);
  }
}
