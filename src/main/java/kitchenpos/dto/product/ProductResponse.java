package kitchenpos.dto.product;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    private ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
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
