package kitchenpos.menu.domain;

import kitchenpos.Product.domain.Product;
import kitchenpos.price.domain.vo.Price;

import javax.persistence.*;
import java.util.Objects;

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

    public MenuProduct(final Menu menu,
                       final Product product,
                       final long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public Price totalPrice() {
        final Price price = product.getPrice();
        return price.multiply(this.quantity);
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
        return quantity;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MenuProduct that = (MenuProduct) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
