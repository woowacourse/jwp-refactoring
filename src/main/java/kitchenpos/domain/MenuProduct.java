package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Column
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

    public Long getProductId() {
        if (Objects.isNull(product)) {
            return null;
        }
        return product.getId();
    }
}
