package kitchenpos.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "MENU_PRODUCT")
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Menu menu, final Product product, final Long quantity) {
        this.seq = null;
        this.menu = menu;
        this.product = product;
        this.quantity = Quantity.from(quantity);
        addMenuProduct(menu);
    }

    private void addMenuProduct(final Menu menu) {
        if (menu != null) {
            menu.getMenuProducts()
                    .add(this);
        }
    }

    public Long getTotalPrice() {
        return this.product.getPrice() * quantity.getQuantity();
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
        return quantity.getQuantity();
    }
}
