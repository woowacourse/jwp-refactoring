package kitchenpos.application.dto;

import java.math.BigDecimal;

public class MenuProductSaveRequest {

    private Long productId;

    private BigDecimal price;

    private int quantity;

    public MenuProductSaveRequest(final Long productId, final BigDecimal price, final int quantity) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
