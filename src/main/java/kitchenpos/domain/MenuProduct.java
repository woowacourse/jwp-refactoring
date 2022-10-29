package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @JoinColumn(name = "menu_id", nullable = false)
    private Long menuId;

    @JoinColumn(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Long menuId, Long productId, long quantity) {
        this(null, menuId, productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
