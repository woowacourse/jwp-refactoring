package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    private Long productId;

    @Column(nullable = false)
    private Long quantity;

    protected MenuProduct() {

    }

    public MenuProduct(Menu menu, Long productId, Long quantity) {
        this(null, menu, productId, quantity);
    }

    public MenuProduct(Long seq, Menu menu, Long productId, Long quantity) {
        this.seq = seq;
        if (Objects.nonNull(menu)) {
            setMenu(menu);
        }
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    private void setMenu(Menu menu) {
        if (Objects.nonNull(this.menu)) {
            this.menu.getMenuProducts().remove(this);
        }
        this.menu = menu;
        menu.getMenuProducts().add(this);
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Long getProductId() {
        return productId;
    }
}
