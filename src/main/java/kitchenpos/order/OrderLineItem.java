package kitchenpos.order;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.vo.Quantity;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long menuId;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, int quantity) {
        this(null, menuId, quantity);
    }

    public OrderLineItem(Long id, Long menuId, int quantity) {
        this.id = id;
        this.menuId = menuId;
        this.quantity = new Quantity(quantity);
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity.getValue();
    }
}
