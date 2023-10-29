package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static java.util.Objects.isNull;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long productId;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Long seq, final Long productId, final long quantity) {
        if (isNull(productId)) {
            throw new IllegalArgumentException("상품이 존재해야 합니다.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("상품의 개수는 양수여야 합니다.");
        }
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(final Long productId, final long quantity) {
        this(null, productId, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
