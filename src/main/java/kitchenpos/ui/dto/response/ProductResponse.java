package kitchenpos.ui.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;

public class ProductResponse {
    private Long id;
    private String name;
    private Double price;

    public ProductResponse() {
    }

    public ProductResponse(final Long id, final String name, final Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(final Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice().getValue().doubleValue()
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

    public Double getPrice() {
        return price;
    }
}
