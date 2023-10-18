package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.dao.entity.ProductEntity;
import kitchenpos.domain.Product;
import kitchenpos.domain.Product2;

public class ProductFixture {

  public static Product2 createProduct() {
    return new Product2("price", BigDecimal.TEN);
  }
}
