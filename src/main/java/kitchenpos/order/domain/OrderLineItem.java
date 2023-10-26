package kitchenpos.order.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.Money;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private long quantity;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private String menuName;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "menu_price", precision = 19, scale = 2, nullable = false))
    private Money menuPrice;

    public OrderLineItem() {
    }

    public OrderLineItem(Long seq, long quantity, Long menuId, String menuName, Money menuPrice) {
        this.seq = seq;
        this.quantity = quantity;
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public Money getMenuPrice() {
        return menuPrice;
    }
}
