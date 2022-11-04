package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    private Order order;
    @Column(name = "menu_id", nullable = false)
    private Long menuId;
    @Column(name = "quantity", nullable = false)
    private long quantity;
    @Column(name = "menu_name", nullable = false)
    private String menuName;
    @Column(name = "menu_group_name", nullable = false)
    private String menuGroupName;
    @Column(name = "menu_price", nullable = false)
    private BigDecimal menuPrice;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final Order order, final Long menuId, final long quantity,
                         final String menuName, final String menuGroupName,
                         final BigDecimal menuPrice) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
        this.menuName = menuName;
        this.menuGroupName = menuGroupName;
        this.menuPrice = menuPrice;
    }

    public OrderLineItem(final Long seq, final Order order, final Long menuId, final long quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(final Long menuId, final String menuName, final String menuGroupName,
                         final BigDecimal menuPrice, final long quantity) {
        this(null, null, menuId, quantity, menuName, menuGroupName, menuPrice);
    }

    public void mapOrder(final Order order) {
        this.order = order;
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

    public long getQuantity() {
        return quantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getMenuGroupName() {
        return menuGroupName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }
}
