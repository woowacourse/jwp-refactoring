package kitchenpos.product.infrastructure.persistence;

import kitchenpos.product.domain.Product;

public class ProductMapper {

  private ProductMapper() {
  }

  public static Product mapToProduct(final ProductEntity entity) {
    return new Product(
        entity.getId(),
        entity.getName(),
        entity.getPrice()
    );
  }
}
