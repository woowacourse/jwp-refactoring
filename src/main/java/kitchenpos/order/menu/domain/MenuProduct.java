package kitchenpos.order.menu.domain;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;
    @JoinColumn(name = "menu_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    private Long productId;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Menu menu,
                       final Long productId,
                       final long quantity) {
        this(null, menu, productId, quantity);
    }

    public MenuProduct(final Long seq,
                       final Menu menu,
                       final Long productId,
                       final long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
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

    public void setMenu(final Menu menu) {
        this.menu = menu;
    }
}
