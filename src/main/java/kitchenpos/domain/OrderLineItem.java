package kitchenpos.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.vo.Quantity;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem(){
    }

    private OrderLineItem(Menu menu, Quantity quantity) {
        this(null, null, menu, quantity);
    }

    private OrderLineItem(Long seq, Order order, Menu menu, Quantity quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(
            Menu menu,
            Long quantity
    ) {
        return new OrderLineItem(
                menu,
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


    public Menu getMenu() {
        return menu;
    }


    public long getQuantity() {
        return quantity.getValue();
    }

}
