package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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

    private String name;

    @Embedded
    private MenuPrice price;

    private long quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final long quantity, final String name, final MenuPrice price,
                         final Menu menu) {
        this.seq = seq;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.menu = menu;
    }

    public static OrderLineItem forSave(final long quantity, final String name, final MenuPrice price,
                                        final Menu menu) {
        return new OrderLineItem(null, quantity, name, price, menu);
    }

    public void joinOrder(final Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }
}
