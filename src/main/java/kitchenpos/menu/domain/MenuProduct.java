package kitchenpos.menu.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private Long productId;

    private long quantity;

    protected MenuProduct() {
    }

    public static MenuProduct create(Long productId, Long quantity) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.productId = productId;
        menuProduct.quantity = quantity;
        return menuProduct;
    }

    public static MenuProduct create(Long seq, Long menuId, Long productId, long quantity) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.seq = seq;
        menuProduct.menu = Menu.createSingleId(menuId);
        menuProduct.productId = productId;
        menuProduct.quantity = quantity;

        return menuProduct;
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

    public void addMenu(Menu menu) {
        this.menu = menu;
    }
}
