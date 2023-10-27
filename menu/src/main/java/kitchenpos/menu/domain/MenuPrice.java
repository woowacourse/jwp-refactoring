package kitchenpos.menu.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class MenuPrice {

    private static final int MIN_PRICE = 0;

    @Column(nullable = false, precision = 19, scale = 2)
    @NotNull
    private BigDecimal price;

    protected MenuPrice() {
    }

    private MenuPrice(BigDecimal price) {
        validate(price);

        this.price = price;
    }

    private void validate(BigDecimal price) {
        if (price == null) {
            throw new NullPointerException("메뉴 금액은 null일 수 없습니다.");
        }
        if (price.doubleValue() < MIN_PRICE) {
            throw new IllegalArgumentException("메뉴 금액은 0원 이상이어야 합니다.");
        }
    }

    public static MenuPrice from(BigDecimal price) {
        return new MenuPrice(price);
    }

    public BigDecimal getPrice() {
        return price;
    }
}
