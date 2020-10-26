package kitchenpos.domain.menu;

import kitchenpos.config.BaseEntity;
import kitchenpos.domain.product.Product;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@AttributeOverride(name = "id", column = @Column(name = "menu_product_id"))
@Entity
public class MenuProduct extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "FK_MENU_PRODUCT_MENU"))
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "FK_MENU_PRODUCT_PRODUCT"))
    private Product product;
    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Long id, Menu menu, Product product, long quantity) {
        this.id = id;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        this(null, menu, product, quantity);
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
