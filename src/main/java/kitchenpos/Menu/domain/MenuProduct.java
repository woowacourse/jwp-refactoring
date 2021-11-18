package kitchenpos.Menu.domain;

import javax.persistence.*;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "product_id")
    private Long productId;

    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long seq, Menu menu, Long productId, long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }
}
