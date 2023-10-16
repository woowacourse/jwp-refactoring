package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductFixture {

  public static Product 깐풍치킨() {
    final Product product = new Product();
    product.setName("깐풍 치킨");
    product.setPrice(BigDecimal.valueOf(10000));
    return product;
  }
}
