package kitchenpos.domain.model.menu;

import java.util.Objects;

import javax.persistence.Entity;

import kitchenpos.domain.model.IdentifiedValueObject;

@Entity
public class MenuProduct extends IdentifiedValueObject {
    private Long productId;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MenuProduct that = (MenuProduct)o;
        return quantity == that.quantity &&
                Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity);
    }
}
