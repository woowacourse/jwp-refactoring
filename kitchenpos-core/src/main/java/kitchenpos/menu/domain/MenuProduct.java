package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
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

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    @Id
    private Long id;

    private long quantity;

    @JoinColumn(name = "menu_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    protected MenuProduct() {
    }

    public MenuProduct(final long quantity,
                       final Menu menu,
                       final Product product) {
        this(null, quantity, menu, product);
    }

    public MenuProduct(final Long id,
                       final long quantity,
                       final Menu menu,
                       final Product product) {
        this.id = id;
        this.quantity = quantity;
        this.menu = menu;
        this.product = product;
    }

    public BigDecimal calculatePrice() {
        return product.getPrice().getValue().multiply(BigDecimal.valueOf(quantity));
    }

    public Long getId() {
        return id;
    }

    public long getQuantity() {
        return quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MenuProduct that = (MenuProduct) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MenuProduct{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", menuId=" + menu.getId() +
                ", productId=" + product.getId() +
                '}';
    }
}
