package kitchenpos.order.domain;

import kitchenpos.menu.domain.MenuProduct;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.util.Objects;

@Embeddable
public class MenuProductSnapshot {
    
    @Embedded
    private ProductSnapshot productSnapshot;
    
    private long quantity;
    
    public MenuProductSnapshot() {
    }
    
    public MenuProductSnapshot(final ProductSnapshot productSnapshot,
                               final long quantity) {
        this.productSnapshot = productSnapshot;
        this.quantity = quantity;
    }
    
    public static MenuProductSnapshot from(final MenuProduct menuProduct) {
        return new MenuProductSnapshot(
                ProductSnapshot.from(menuProduct.getProduct()),
                menuProduct.getQuantity());
    }
    
    public ProductSnapshot getProductSnapshot() {
        return productSnapshot;
    }
    
    public long getQuantity() {
        return quantity;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProductSnapshot that = (MenuProductSnapshot) o;
        return quantity == that.quantity
                && productSnapshot.equals(that.productSnapshot);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(productSnapshot, quantity);
    }
}
