package kitchenpos.domain;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class ProductQuantity {

    private final Product product;
    private final Integer quantity;

    public ProductQuantity(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return product.getId();
    }

    public BigDecimal calculateTotalPrice() {
        return this.product.calculateTotalPriceFromQuantity(this.quantity);
    }
}
