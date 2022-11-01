package kitchenpos.domain;

import java.math.BigDecimal;

public class PendingMenuProduct {

    private final long productId;
    private final BigDecimal price;
    private final long quantity;

    public PendingMenuProduct(final long productId, final BigDecimal price, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public MenuProduct createMenuProduct() {
        return new MenuProduct(productId, quantity);
    }
}
