package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class MenuPrice {
    private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;
    private static final BigDecimal MAX_PRICE = BigDecimal.valueOf(Math.pow(10, 17));

    private BigDecimal price;

    public MenuPrice() {
    }

    public MenuPrice(final BigDecimal price) {
        validation(price);
        this.price = price;
    }

    private void validation(final BigDecimal price) {
        if (Objects.isNull(price) || isLessThanMinPrice(price) || isMoreThanMaxPrice(price)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isMoreThanMaxPrice(final BigDecimal price) {
        return price.compareTo(MAX_PRICE) >= 0;
    }

    private boolean isLessThanMinPrice(final BigDecimal price) {
        return price.compareTo(MIN_PRICE) < 0;
    }

    public void compareTo(final BigDecimal menuProductsPrice) {
        if (price.compareTo(menuProductsPrice) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 메뉴 상품 금액들의 합보다 작거나 같아야 합니다.");
        }
    }

    public BigDecimal getPrice() {
        return price;
    }
}
