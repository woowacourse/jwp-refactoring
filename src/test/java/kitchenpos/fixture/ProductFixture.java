package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import kitchenpos.common.TestObjectUtils;
import kitchenpos.domain.Product;

public class ProductFixture {
    public static final Product FRIED_CHICKEN = TestObjectUtils.createProduct(1L, "후라이드",
            BigDecimal.valueOf(15000));

    public static final Product COLA = TestObjectUtils.createProduct(2L, "콜라",
            BigDecimal.valueOf(1000));

    public static final List<Product> PRODUCTS = Arrays.asList(FRIED_CHICKEN, COLA);

    public static final Product NEGATIVE_PRICE_PRODUCT = TestObjectUtils.createProduct(1L, "후라이드",
            BigDecimal.valueOf(-1));

    public static final Product NULL_PRICE_PRODUCT = TestObjectUtils.createProduct(1L, "후라이드",
            BigDecimal.valueOf(-1));
}
