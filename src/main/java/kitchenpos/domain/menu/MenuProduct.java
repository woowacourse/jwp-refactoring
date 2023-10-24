package kitchenpos.domain.menu;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.common.Name;
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

    @Embedded
    private Price menuPrice;

    @Embedded
    private Name menuName;

    @Column(name = "product_id")
    private Long productId;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Long productId, final Price menuPrice, final Name menuName, final long quantity) {
        this.productId = productId;
        this.menuPrice = menuPrice;
        this.menuName = menuName;
        this.quantity = new Quantity(quantity);
    }

    public void initMenu(final Menu menu) {
        this.menu = menu;
    }

    public Price calculateMenuProductPrice() {
        return this.menuPrice.times(this.quantity.value());
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
