package kitchenpos.order;

import kitchenpos.common.vo.Price;
import kitchenpos.menu.Menu;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderLineItem {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long seq;

    @Column
    private long quantity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_to_order"))
    private Order order;

    @Column
    private Long menuId;

    @Column
    private String menuName;

    @Embedded
    private Price menuPrice;

    protected OrderLineItem() {
    }

    private OrderLineItem(final Long seq, final long quantity, final Order order, final Long menuId, final String menuName, final Price menuPrice) {
        this.seq = seq;
        this.quantity = quantity;
        this.order = order;
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public OrderLineItem(final long quantity, final Order order, final Menu menu) {
        this(null, quantity, order, menu.getId(), menu.getName(), menu.getPrice());
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }
}
