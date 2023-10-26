package kitchenpos.product.application.dto;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductUpdateRequest {

    private Long productId;
    private String name;
    private BigDecimal price;

    private ProductUpdateRequest() {
    }

    public ProductUpdateRequest(final Long productId, final String name, final BigDecimal price) {
        this.productId = productId;
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return Product.of(name, price);
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
