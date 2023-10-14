package kitchenpos.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import kitchenpos.domain.Product;

public class ProductFixtures {

    /**
     * NAME
     */
    public static final String PRODUCT1_NAME = "후라이드";
    public static final String PRODUCT2_NAME = "양념치킨";

    /**
     * PRICE
     */
    public static final BigDecimal PRODUCT1_PRICE = BigDecimal.valueOf(16000).setScale(0, RoundingMode.UNNECESSARY);
    public static final BigDecimal PRODUCT2_PRICE = BigDecimal.valueOf(1000).setScale(0, RoundingMode.UNNECESSARY);

    /**
     * REQUEST
     */
    public static Product PRODUCT1_REQUEST() {
        Product product = new Product();
        product.setName(PRODUCT1_NAME);
        product.setPrice(PRODUCT1_PRICE);
        return product;
    }

    public static Product PRODUCT2_REQUEST() {
        Product product = new Product();
        product.setName(PRODUCT2_NAME);
        product.setPrice(PRODUCT2_PRICE);
        return product;
    }
}
