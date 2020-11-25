package kitchenpos.domain.model.order;

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
}
