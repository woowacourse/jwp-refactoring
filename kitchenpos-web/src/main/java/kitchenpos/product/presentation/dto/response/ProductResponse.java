package kitchenpos.product.presentation.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;

public class ProductResponse {

    private Long id;

    private String name;

    private long price;

    private ProductResponse(final Long id,
                            final String name,
                            final long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse from(final Product product) {
        return new ProductResponse(product.getId(),
                                   product.getName(),
                                   product.getPrice().getValue().longValue());
    }

    public static List<ProductResponse> convertToList(final List<Product> products) {
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

    public long getPrice() {
        return price;
    }
}
