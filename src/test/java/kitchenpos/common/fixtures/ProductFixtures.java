package kitchenpos.common.fixtures;

import java.math.BigDecimal;
import java.math.RoundingMode;
import kitchenpos.dto.product.ProductCreateRequest;

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
    public static ProductCreateRequest PRODUCT1_REQUEST() {
        return new ProductCreateRequest(PRODUCT1_NAME, PRODUCT1_PRICE);
    }

    public static ProductCreateRequest PRODUCT2_REQUEST() {
        return new ProductCreateRequest(PRODUCT2_NAME, PRODUCT2_PRICE);
    }
}
