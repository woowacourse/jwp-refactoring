package kitchenpos.order.domain.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.vo.Price;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_line_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Order order;

    private Long menuId;

    @Embedded
    private Price price;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, long menuPrice, long quantity) {
        this.menuId = menuId;
        this.price = new Price(menuPrice * quantity);
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Price getPrice() {
        return price;
    }

    public Order getOrder() {
        return order;
    }

    public long getQuantity() {
        return quantity;
    }

    public void associateOrder(Order order) {
        this.order = order;
    }
}
