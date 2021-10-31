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
            throw new FieldNotValidException("상품이 유효하지 않습니다.");
        }
    }

    private void validateQuantity(Long quantity) {
        if (Objects.isNull(quantity)) {
            throw new FieldNotValidException("상품 수량이 유효하지 않습니다.");
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
