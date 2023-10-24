package kitchenpos.domain.menu;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.common.Price;
import kitchenpos.domain.common.Quantity;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Product product, final long quantity) {
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public void initMenu(final Menu menu) {
        this.menu = menu;
    }

    public Price productPrice() {
        return product.price();
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
        return quantity.getValue();
    }

    @Override
    public boolean equals(final Object target) {
        if (this == target) {
            return true;
        }
        if (target == null || getClass() != target.getClass()) {
            return false;
        }
        final MenuProduct targetMenuProduct = (MenuProduct) target;
        return Objects.equals(getSeq(), targetMenuProduct.getSeq());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSeq());
    }
}
