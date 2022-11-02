package kitchenpos.menu.domain;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class MenuProductQuantity {

    private final Product product;
    private final long quantity;

    public MenuProductQuantity(final Product product, final long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return product.getCalculatedPrice(quantity);
    }
}
