package kitchenpos.order;

import kitchenpos.common.Price;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long menuId;
    private long quantity;
    private String orderedName;
    @Embedded
    @AttributeOverride(
            name = "amount",
            column = @Column(name = "orderedPrice", precision = 19, scale = 2, nullable = false)
    )
    private Price orderedPrice;

    public OrderLineItem() {
    }

    public OrderLineItem(Long menuId, long quantity, String orderedName, Price orderedPrice) {
        this.menuId = menuId;
        this.quantity = quantity;
        this.orderedName = orderedName;
        this.orderedPrice = orderedPrice;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getOrderedName() {
        return orderedName;
    }

    public Price getOrderedPrice() {
        return orderedPrice;
    }
}
