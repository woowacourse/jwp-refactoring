package kitchenpos.product.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;

public class ProductQueryResponse {

  private Long id;
  private String name;
  private BigDecimal price;

  public ProductQueryResponse(final Long id, final String name, final BigDecimal price) {
    this.id = id;
    this.name = name;
    this.price = price;
  }

  public ProductQueryResponse() {
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

  public static List<ProductQueryResponse> of(final List<Product> products) {
    return products.stream()
        .map(product -> new ProductQueryResponse(product.getId(), product.getName(),
            product.getPrice()))
        .collect(Collectors.toList());
  }
}
