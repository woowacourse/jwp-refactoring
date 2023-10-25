package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "menu_id")
    private Long menuId;

    private Long quantity;

    private BigDecimal orderedPrice;

    public OrderLineItem(Long menuId, Long quantity, BigDecimal orderedPrice) {
        this.menuId = menuId;
        this.quantity = quantity;
        this.orderedPrice = orderedPrice;
    }

    protected OrderLineItem() {
    }

    public Long getSeq() {
        return seq;
    }


    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public BigDecimal getOrderedPrice() {
        return orderedPrice;
    }
}
