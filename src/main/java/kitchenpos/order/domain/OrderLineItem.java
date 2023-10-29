package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.common.vo.Price;
import kitchenpos.menu.domain.Menu;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "menu_name")
    private String menuName;

    @Embedded
    @AttributeOverride(name = "price", column = @Column(name = "menu_price", precision = 19, scale = 2))
    private Price menuPrice;

    @Column(nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(
            final Order order,
            final Long menuId,
            final long quantity
    ) {
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(
            final Long menuId,
            final String menuName,
            final BigDecimal menuPrice,
            final long quantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = Price.from(menuPrice);
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
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

    public void setOrder(final Order order) {
        this.order = order;
    }
}
