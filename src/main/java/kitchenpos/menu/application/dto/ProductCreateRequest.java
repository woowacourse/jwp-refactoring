package kitchenpos.menu.application.dto;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;

public class ProductCreateRequest {

  private String name;
  private BigDecimal price;

  public ProductCreateRequest(final String name, final BigDecimal price) {
    this.name = name;
    this.price = price;
  }

  public ProductCreateRequest() {
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public Product toProduct() {
    return new Product(name, new Price(price));
  }
}
