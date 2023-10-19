package kitchenpos.product.application.dto;

import java.math.BigDecimal;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductPersistence {

  private Long id;
  private String name;
  private BigDecimal price;

  public ProductPersistence(final Long id, final String name, final BigDecimal price) {
    this.id = id;
    this.name = name;
    this.price = price;
  }

  public ProductPersistence() {
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public static ProductPersistence from(final Product product) {
    return new ProductPersistence(product.getId(), product.getName(),
        product.getPrice().getValue());
  }

  public Product toProduct() {
    return new Product(id, name, new Price(price));
  }
}
