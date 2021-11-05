package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Long quantity;

    protected MenuProduct() {

    }

    public MenuProduct(Menu menu, ProductQuantity productQuantity) {
        this(null, menu, productQuantity.getProduct(), productQuantity.getQuantity());
    }

    public MenuProduct(Long seq, Menu menu, Product product, Long quantity) {
        this.seq = seq;
        setMenu(menu);
        this.product = product;
        this.quantity = quantity;
    }

    private void setMenu(Menu menu) {
        if (Objects.nonNull(this.menu)) {
            this.menu.getMenuProducts().remove(this);
        }
        this.menu = menu;
        menu.getMenuProducts().add(this);
    }

    public void addMenu(Menu menu) {
        setMenu(menu);
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Long getProductId() {
        return product.getId();
    }
}
