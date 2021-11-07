package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    private static final long ID = 1L;
    private static final String NAME = "이달의치킨";
    private static final BigDecimal PRICE = BigDecimal.valueOf(20_000);

    public static Product create() {
        return create(ID, NAME, PRICE);
    }

    public static Product create(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);

        return product;
    }
}
