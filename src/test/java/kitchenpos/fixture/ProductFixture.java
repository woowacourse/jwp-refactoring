package kitchenpos.fixture;

import java.math.BigDecimal;

import kitchenpos.domain.Product;

public class ProductFixture {

    public static final Long ID1 = 1L;
    public static final Long ID2 = 2L;
    public static final String NAME1 = "chicken";
    public static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(18000L);
    public static final BigDecimal NEGATIVE_PRICE = BigDecimal.valueOf(-18000L);

    public static Product createWithoutId() {
        Product product = new Product();
        product.setName(NAME1);
        product.setPrice(DEFAULT_PRICE);

        return product;
    }

    public static Product createWithId(Long id) {
        Product product = new Product();
        product.setId(id);
        product.setName(NAME1);
        product.setPrice(DEFAULT_PRICE);

        return product;
    }

    public static Product createNegativePriceWithId(Long id) {
        Product product = new Product();
        product.setId(id);
        product.setName(NAME1);
        product.setPrice(NEGATIVE_PRICE);

        return product;
    }

    public static Product createNullPriceWithId(Long id) {
        Product product = new Product();
        product.setId(id);
        product.setName(NAME1);

        return product;
    }
}
