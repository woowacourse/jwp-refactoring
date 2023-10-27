package kitchenpos.domain;

import common.domain.Price;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String name;

    @Embedded
    private Price price;

    private long quantity;

    @Column(name = "menu_id")
    private Long menuId;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final long quantity, final String name, final Price price,
                         final Long menuId) {
        this.seq = seq;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.menuId = menuId;
    }

    public static OrderLineItem forSave(final long quantity, final String name, final Price price,
                                        final Long menuId) {
        return new OrderLineItem(null, quantity, name, price, menuId);
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

    public Long getMenuId() {
        return menuId;
    }
}
