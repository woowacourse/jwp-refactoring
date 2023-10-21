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
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Embedded
    private Quantity quantity;

    public OrderLineItem(){
    }

    public OrderLineItem(Menu menu, Quantity quantity) {
        this(null, null, menu, quantity);
    }

    public OrderLineItem(Long seq, Orders orders, Menu menu, Quantity quantity) {
        this.seq = seq;
        this.orders = orders;
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

    public void registerOrders(Orders orders) {
        this.orders = orders;
    }

    public Long getSeq() {
        return seq;
    }


    public Orders getOrder() {
        return orders;
    }


    public Menu getMenu() {
        return menu;
    }


    public long getQuantity() {
        return quantity.getValue();
    }

}
