package kitchenpos.application.vo;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductQuantity {
    private final Product product;
    private final Long quantity;

    public ProductQuantity(Product product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public BigDecimal multiply() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
