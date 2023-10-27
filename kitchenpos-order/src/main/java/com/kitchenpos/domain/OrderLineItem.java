package com.kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "menu_id")
    private Long menuId;

    @Embedded
    private Quantity quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long menuId, final Long quantity) {
        this.menuId = menuId;
        this.quantity = Quantity.from(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity.getQuantity();
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderLineItem)) return false;
        OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(seq, that.seq) && Objects.equals(menuId, that.menuId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menuId, quantity);
    }
}
