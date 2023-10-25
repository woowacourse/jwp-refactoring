package kitchenpos.domain.menu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.vo.Price;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(optional = false)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_to_menu"))
    private Menu menu;
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_to_product"))
    private Product product;
    @Column(nullable = false)
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Product product,
                       final long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Price totalPrice() {
        final Price price = product.getPrice();

        return price.multiply(this.quantity);
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
