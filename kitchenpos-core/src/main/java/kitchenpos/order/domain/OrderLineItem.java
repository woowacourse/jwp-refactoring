package kitchenpos.order.domain;

import kitchenpos.menu.domain.vo.Price;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Column(name = "menu_id", nullable = false)
    private Long menuId;
    @Column(nullable = false)
    private String menuName;
    @Embedded
    private Price menuPrice;
    @Column(nullable = false)
    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(final Long menuId, final String menuName, final BigDecimal menuPrice, final Long quantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = new Price(menuPrice);
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public Price getMenuPrice() {
        return menuPrice;
    }

    public long getQuantity() {
        return quantity;
    }
}
