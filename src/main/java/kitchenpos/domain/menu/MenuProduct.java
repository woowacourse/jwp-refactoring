package kitchenpos.domain.menu;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long productId;
    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Long seq, Long productId, long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
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
