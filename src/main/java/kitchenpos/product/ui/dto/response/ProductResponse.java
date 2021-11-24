package kitchenpos.product.ui.dto.response;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    private ProductResponse() {
    }

    private ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse create(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    public static List<ProductResponse> of(List<Product> products) {
        return products.stream()
                .map(ProductResponse::create)
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
