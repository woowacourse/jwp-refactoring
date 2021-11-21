package kitchenpos.product.domain;

import java.math.BigDecimal;

public class MenuProductEvent {
    private final Long productId;
    private final BigDecimal productPrice;

    public MenuProductEvent(Long productId, BigDecimal productPrice) {
        this.productId = productId;
        this.productPrice = productPrice;
    }

    public Long getProductId() {
        return productId;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }
}
