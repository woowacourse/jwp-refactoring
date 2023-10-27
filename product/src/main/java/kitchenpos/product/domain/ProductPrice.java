package kitchenpos.product.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class ProductPrice {

    private static final int MIN_PRICE = 0;

    @Column(precision = 19, scale = 2)
    @NotNull
    private BigDecimal price;

    protected ProductPrice() {
    }

    private ProductPrice(BigDecimal price) {
        validate(price);

        this.price = price;
    }

    private void validate(BigDecimal price) {
        if (price == null) {
            throw new NullPointerException("상품 금액은 null일 수 없습니다.");
        }
        if (price.doubleValue() < MIN_PRICE) {
            throw new IllegalArgumentException("상품 금액은 0원 이상이어야 합니다.");
        }
    }

    public static ProductPrice from(BigDecimal price) {
        return new ProductPrice(price);
    }

    public BigDecimal getPrice() {
        return price;
    }

}
