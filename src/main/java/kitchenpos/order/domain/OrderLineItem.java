package kitchenpos.order.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.generic.Money;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long menuId;
    private String menuName;
    private Money menuPrice;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, String menuName, Money menuPrice, long quantity) {
        this(null, menuId, menuName, menuPrice, quantity);
    }

    private OrderLineItem(Long seq, Long menuId, String menuName, Money menuPrice, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public Money getMenuPrice() {
        return menuPrice;
    }
}
