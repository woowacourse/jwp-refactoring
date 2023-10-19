package kitchenpos.dao.mapper;

import kitchenpos.dao.entity.ProductEntity;
import kitchenpos.domain.Product;

public class ProductMapper {

  private ProductMapper() {
  }

  public static Product mapToProduct(final ProductEntity entity) {
    return new Product(
        entity.getName(),
        entity.getPrice()
    );
  }
}
