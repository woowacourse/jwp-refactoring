package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static final Long PRODUCT_ID_ONE = 1L;
    public static final Long PRODUCT_ID_TWO = 2L;
    public static final Long PRODUCT_ID_THREE = 3L;
    public static final String PRODUCT_NAME = "맛잇는 빵";
    public static final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(1000);
    public static final Product UNSAVED_PRODUCT = new Product(PRODUCT_NAME, PRODUCT_PRICE);
}
