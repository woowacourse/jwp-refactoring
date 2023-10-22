package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    
    private long quantity;
    
    public OrderLineItem(final Order order,
                         final Menu menu,
                         final long quantity) {
        this(null, order, menu, quantity);
    }
    
    public OrderLineItem(final Long seq,
                         final Order order,
                         final Menu menu,
                         final long quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }
    
    public Order getOrder() {
        return order;
    }
    
    public Menu getMenu() {
        return menu;
    }
    
    public long getQuantity() {
        return quantity;
    }
}
