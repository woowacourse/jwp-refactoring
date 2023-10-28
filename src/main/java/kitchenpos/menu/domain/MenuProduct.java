package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    private Menu menu;

    @OneToOne
    private Product product;

    @Embedded
    private Quantity quantity;

    public MenuProduct() {
    }

    public MenuProduct(final Product product, final Quantity quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct create(final Product product, final long quantity) {
        return new MenuProduct(product, Quantity.create(quantity));
    }

    public void updateMenu(Menu menu) {
        this.menu = menu;
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


    public long getQuantity() {
        return quantity.getQuantity();
    }

    public BigDecimal getPrice() {
        return product.getPrice().multiply(new BigDecimal(quantity.getQuantity()));
    }
}
