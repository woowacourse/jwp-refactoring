package kitchenpos.common.event;

import java.util.List;

public class ValidatorProductBeing {

    private final List<Long> productIds;

    public ValidatorProductBeing(final List<Long> productIds) {
        this.productIds = productIds;
    }

    public List<Long> getProductIds() {
        return productIds;
    }
}
