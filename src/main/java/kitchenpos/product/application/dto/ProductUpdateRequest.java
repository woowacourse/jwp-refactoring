package kitchenpos.product.application.dto;

import java.math.BigDecimal;

public class ProductUpdateRequest extends ProductRequest {

    private Long productId;

    private ProductUpdateRequest() {
        super();
    }

    public ProductUpdateRequest(final Long productId, final String name, final BigDecimal price) {
        super(name, price);
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
