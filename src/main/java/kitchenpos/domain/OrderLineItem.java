package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long menuId;
    private int quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, int quantity) {
        this(null, menuId, quantity);
    }

    public OrderLineItem(Long id, Long menuId, int quantity) {
        this.id = id;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }
}
