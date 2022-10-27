package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public enum ProductFixture {
    불맛_떡볶이("불맛 떡볶이", BigDecimal.valueOf(8000)),
    짜장맛_떡볶이("짜장맛 떡볶이", BigDecimal.valueOf(7000)),
    카레맛_떡볶이("카레맛 떡볶이", BigDecimal.valueOf(9000)),
    공짜_어묵국물("어묵 국물", BigDecimal.ZERO),
    ;

    private final String name;
    private final BigDecimal price;

    ProductFixture(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return new Product(null, name, price);
    }

    public Product toEntity(final BigDecimal price) {
        return new Product(null, name, price);
    }

    public BigDecimal getPrice() {
        return price;
    }
}
