package kitchenpos.domain.model.order;

import java.util.Objects;

import javax.persistence.Entity;

import kitchenpos.domain.model.IdentifiedValueObject;

@Entity
public class OrderLineItem extends IdentifiedValueObject {
    private Long menuId;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
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
        OrderLineItem that = (OrderLineItem)o;
        return quantity == that.quantity &&
                Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, quantity);
    }
}
