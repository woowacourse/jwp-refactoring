package kitchenpos.menu.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.vo.Quantity;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private long productId;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Menu menu, long productId, Quantity quantity) {
        this(null, menu, productId, quantity);
    }

    public MenuProduct(Long seq, Menu menu, long productId, Quantity quantity) {
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long seq() {
        return seq;
    }

    public Menu menu() {
        return menu;
    }

    public Long productId() {
        return productId;
    }

    public Quantity quantity() {
        return quantity;
    }
}
