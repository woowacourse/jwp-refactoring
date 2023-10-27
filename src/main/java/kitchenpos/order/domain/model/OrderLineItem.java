package kitchenpos.order.domain.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.menu.domain.model.Menu;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    private Long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Menu menu, Long quantity) {
        this(null, menu, quantity);
    }

    public OrderLineItem(Long seq, Menu menu, Long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }
    
    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getQuantity() {
        return quantity;
    }
}
