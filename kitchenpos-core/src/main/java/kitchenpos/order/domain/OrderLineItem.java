package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Price price;

    @Column(nullable = false)
    private Long menuId;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem(){
    }

    private OrderLineItem(String name, Price price, Long menuId, Quantity quantity) {
        this(null, null, name, price, menuId, quantity);
    }

    public OrderLineItem(
            Long seq,
            Order order,
            String name,
            Price price,
            Long menuId,
            Quantity quantity
    ) {
        this.seq = seq;
        this.order = order;
        this.name = name;
        this.price = price;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem of(
            String name,
            BigDecimal price,
            Long menuId,
            Long quantity
    ) {
        return new OrderLineItem(
                name,
                Price.from(price),
                menuId,
                Quantity.from(quantity)
        );
    }

    public void registerOrders(Order order) {
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
        return quantity.getValue();
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public String getName() {
        return name;
    }

}
