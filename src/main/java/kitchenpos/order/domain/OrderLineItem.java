package kitchenpos.order.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private BigDecimal menuPrice;

    @Column(nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Order order, Menu menu, long quantity) {
        this.order = order;
        this.menuName = menu.getName();
        this.menuPrice = menu.getPrice();
        this.quantity = quantity;
    }
}
