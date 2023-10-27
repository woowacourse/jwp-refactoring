package kitchenpos.ui.response;

import kitchenpos.domain.product.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ProductResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;

    private ProductResponse(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(final Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }

    public static List<ProductResponse> from(final List<Product> products) {
        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
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
