package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import suppoert.domain.BaseEntity;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_menu_product_to_menu"))

    private Menu menu;
    @OneToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_menu_product_to_product"))

    private Product product;
    @Column(nullable = false)
    private long quantity;

    public MenuProduct(final Menu menu, final Product product, final long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getProduct() {
        return product.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    protected MenuProduct() {
    }
}
