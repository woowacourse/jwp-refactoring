package kitchenpos.menu.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Product;

public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    public static List<ProductResponse> toList(List<Product> products) {
        return products.stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getPrice()))
                .collect(Collectors.toList());
    }

    protected ProductResponse() {
    }

    private ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }
}
