package kitchenpos.domain.order;

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.vo.Price;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String menuName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(nullable = false, name = "menu_price"))
    })
    private Price menuPrice;

    @Column(nullable = false)
    private Long menuId;
    @Column(nullable = false)
    private Long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Order order, final String menuName, final BigDecimal menuPrice, final Long menuId,
                         final Long quantity) {
        this.order = order;
        this.menuName = menuName;
        this.menuPrice = Price.valueOf(menuPrice);
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(final String menuName, final BigDecimal menuPrice, final Long menuId, final Long quantity) {
        this(null, menuName, menuPrice, menuId, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public Order getOrder() {
        return order;
    }

    public String getMenuName() {
        return menuName;
    }

    public Price getMenuPrice() {
        return menuPrice;
    }

    public BigDecimal getMenuPriceValue() {
        return menuPrice.getValue();
    }
}
