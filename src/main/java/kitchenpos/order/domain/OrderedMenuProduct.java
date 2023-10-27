package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Price;

@Entity
public class OrderedMenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(nullable = false)
    private OrderedMenu orderedMenu;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @Column(nullable = false)
    private long quantity;

    protected OrderedMenuProduct() {
    }

    public OrderedMenuProduct(OrderedMenu orderedMenu, String name, Price price, long quantity) {
        this(null, orderedMenu, name, price, quantity);
    }

    public OrderedMenuProduct(Long seq, OrderedMenu orderedMenu, String name, Price price, long quantity) {
        this.seq = seq;
        this.orderedMenu = orderedMenu;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public OrderedMenu getOrderedMenu() {
        return orderedMenu;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}
