package kitchenpos.domain;

import kitchenpos.exception.FieldNotValidException;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductQuantity {
    private final Product product;
    private final Long quantity;

    public ProductQuantity(Product product, Long quantity) {
        validate(product, quantity);
        this.product = product;
        this.quantity = quantity;
    }

    private void validate(Product product, Long quantity) {
        validateProduct(product);
        validateQuantity(quantity);
    }

    private void validateProduct(Product product) {
        if (Objects.isNull(product)) {
            throw new FieldNotValidException(this.getClass().getSimpleName(), "product");
        }
    }

    private void validateQuantity(Long quantity) {
        if (Objects.isNull(quantity)) {
            throw new FieldNotValidException(this.getClass().getSimpleName(), "quantity");
        }
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
