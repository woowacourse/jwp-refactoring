package kitchenpos.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

public class MenuProduct {
    @Id
    private Long seq;
    private AggregateReference<Product, Long> productId;
    private long quantity;

    public MenuProduct(final Long seq,final Long productId, final long quantity) {
        this.seq = seq;
        this.productId = () -> productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId.getId();
    }

    public long getQuantity() {
        return quantity;
    }
}
