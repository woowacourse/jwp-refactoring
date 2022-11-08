package kitchenpos.order.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @Column(name = "menu_name")
    private String menuName;

    @Embedded
    private Price price;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(String menuName, BigDecimal price, long quantity) {
        this.menuName = menuName;
        this.price = new Price(price);
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getMenuName() {
        return menuName;
    }

    public Price getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}

