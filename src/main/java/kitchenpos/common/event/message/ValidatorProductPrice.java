package kitchenpos.common.event.message;

import java.util.List;

public class ValidatorProductPrice {

    private final List<Long> productIds;
    private final Long price;

    public ValidatorProductPrice(final List<Long> productIds, final Long price) {
        this.productIds = productIds;
        this.price = price;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public Long getPrice() {
        return price;
    }
}
