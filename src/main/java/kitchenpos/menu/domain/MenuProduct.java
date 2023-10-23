package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;


public class MenuProduct {
    @Id
    private Long seq;
    private AggregateReference<Product, Long> productId;
    private long quantity;

    private MenuProduct() {
    }

    public MenuProduct(Long productId, long quantity) {
        this(null, productId, quantity);
    }

    private MenuProduct(Long seq, Long productId, long quantity) {
        this.seq = seq;
        this.productId = AggregateReference.to(productId);
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public AggregateReference<Product, Long> getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
