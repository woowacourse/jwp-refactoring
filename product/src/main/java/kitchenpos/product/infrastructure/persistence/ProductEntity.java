package kitchenpos.product.infrastructure.persistence;

import java.math.BigDecimal;

public class ProductEntity {

  private Long id;
  private String name;
  private BigDecimal price;

  public ProductEntity(final Long id, final String name, final BigDecimal price) {
    this.id = id;
    this.name = name;
    this.price = price;
  }

  public ProductEntity(final String name, final BigDecimal price) {
    this(null, name, price);
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
}
