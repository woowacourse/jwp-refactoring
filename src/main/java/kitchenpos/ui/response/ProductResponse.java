package kitchenpos.ui.response;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;

public class ProductResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    public ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getPrice()
        );
    }

    public static List<ProductResponse> of(List<Product> products) {
        return products.stream()
            .map(ProductResponse::from)
            .collect(toList());
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
