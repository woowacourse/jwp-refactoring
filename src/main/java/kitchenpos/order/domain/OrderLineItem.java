package kitchenpos.order.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.Hibernate;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private BigDecimal menuPrice;

    @Column
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Order order, String menuName, BigDecimal menuPrice, long quantity) {
        this.order = order;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        OrderLineItem orderLineItem = (OrderLineItem)o;
        return seq != null && Objects.equals(seq, orderLineItem.getSeq());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
