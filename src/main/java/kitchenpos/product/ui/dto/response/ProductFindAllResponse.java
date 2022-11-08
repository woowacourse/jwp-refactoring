package kitchenpos.product.ui.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;

public class ProductFindAllResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    public ProductFindAllResponse() {
    }

    private ProductFindAllResponse(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static List<ProductFindAllResponse> from(final List<Product> products) {
        return products.stream()
                .map(ProductFindAllResponse::from)
                .collect(Collectors.toList());
    }

    private static ProductFindAllResponse from(final Product product) {
        return new ProductFindAllResponse(product.getId(), product.getName(), product.getPrice());
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
