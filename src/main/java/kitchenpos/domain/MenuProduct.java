package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long productId;
    private long quantity;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    public MenuProduct() {
    }

    public MenuProduct(final Long seq, final Long productId, final long quantity, final Menu menu) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
        this.menu = menu;
    }

    public MenuProduct(final Long seq, final Long productId, final long quantity) {
        this(seq, productId, quantity, null);
    }

    public MenuProduct(final Long productId, final long quantity) {
        this(null, productId, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(final Menu menu) {
        this.menu = menu;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
