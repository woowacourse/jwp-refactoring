package kitchenpos.fixture;

import java.math.BigDecimal;

import kitchenpos.domain.Product;

public class ProductFixture {

    public static final Long ID1 = 1L;
    public static final Long ID2 = 2L;
    public static final String NAME1 = "chicken";
    public static final Long PRICE1 = 18000L;

    public static Product createWithoutId(long price) {
        Product product = new Product();
        product.setName(NAME1);
        product.setPrice(BigDecimal.valueOf(price));

        return product;
    }

    public static Product createWithId(Long id, Long price) {
        Product product = new Product();
        product.setId(id);
        product.setName(NAME1);
        product.setPrice(BigDecimal.valueOf(price));

        return product;
    }
}
