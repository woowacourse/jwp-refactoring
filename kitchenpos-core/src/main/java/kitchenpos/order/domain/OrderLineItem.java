package kitchenpos.order.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    
    @Embedded
    private MenuSnapshot menuSnapshot;
    
    private long quantity;
    
    protected OrderLineItem() {
    }
    
    public OrderLineItem(final Order order,
                         final MenuSnapshot menuSnapshot,
                         final long quantity) {
        this(null, order, menuSnapshot, quantity);
    }
    
    public OrderLineItem(final Long seq,
                         final Order order,
                         final MenuSnapshot menuSnapshot,
                         final long quantity) {
        this.seq = seq;
        this.order = order;
        this.menuSnapshot = menuSnapshot;
        this.quantity = quantity;
    }
    
    public Long getSeq() {
        return seq;
    }
    
    public Order getOrder() {
        return order;
    }
    
    public MenuSnapshot getMenuSnapshot() {
        return menuSnapshot;
    }
    
    public long getQuantity() {
        return quantity;
    }
    
    public void setOrder(final Order order) {
        this.order = order;
    }
}
