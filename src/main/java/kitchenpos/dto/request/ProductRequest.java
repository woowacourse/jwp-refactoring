package kitchenpos.dto.request;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductRequest {

    private String name;
    private BigDecimal price;

    public ProductRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return product;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    private ProductRequest() {
    }
}
