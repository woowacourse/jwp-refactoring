package kitchenpos.order.domain;

import kitchenpos.menu.domain.MenuPrice;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    private String menuName;
    @Embedded
    private MenuPrice menuPrice;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Order order, String menuName, MenuPrice menuPrice, long quantity) {
        this.order = order;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }
}
