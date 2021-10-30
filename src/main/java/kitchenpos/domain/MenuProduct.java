package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue
    private Long seq;

    @ManyToOne
    private Menu menu;
    private Long productId;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long productId, long quantity) {
        this(null, null, productId, quantity);
    }

    private MenuProduct(Long seq, Menu menu, Long productId, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public void setMenu(final Menu menu) {
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
