package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Long menuId;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, long quantity) {
        this(null, null, menuId, null, null, quantity);
    }

    public OrderLineItem(
            Order order,
            Long menuId,
            Name name,
            Price price,
            long quantity
    ) {
        this(null, order, menuId, name, price, quantity);
    }

    public OrderLineItem(Long seq, Order order, Long menuId, Name name, Price price, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.quantity = new Quantity(quantity);
    }

    public void assignMenuId(Long id) {
        this.menuId = id;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity.getQuantity();
    }
}
