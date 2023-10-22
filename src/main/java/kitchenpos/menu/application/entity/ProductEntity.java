package kitchenpos.menu.application.entity;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;

public class ProductEntity {

  private Long id;
  private String name;
  private BigDecimal price;

  public ProductEntity(final Long id, final String name, final BigDecimal price) {
    this.id = id;
    this.name = name;
    this.price = price;
  }

  public ProductEntity() {
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

  public static ProductEntity from(final Product product) {
    return new ProductEntity(product.getId(), product.getName(),
        product.getPrice().getValue());
  }

  public Product toProduct() {
    return new Product(id, name, new Price(price));
  }
}
