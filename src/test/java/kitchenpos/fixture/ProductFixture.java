package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public enum ProductFixture {
    불맛_떡볶이("불맛 떡볶이", BigDecimal.valueOf(8000)),
    짜장맛_떡볶이("짜장맛 떡볶이", BigDecimal.valueOf(7000)),
    카레맛_떡볶이("카레맛 떡볶이", BigDecimal.valueOf(9000)),
    ;

    private final String name;
    private final BigDecimal price;

    ProductFixture(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
