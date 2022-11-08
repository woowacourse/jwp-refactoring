package kitchenpos.domain.order;

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long menuId;

    @Embedded
    private Quantity quantity;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "menu_name"))
    private Name menuName;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "menu_price"))
    private Price price;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long menuId, final long quantity) {
        this(null, menuId, new Quantity(quantity), null, null);
    }

    public OrderLineItem(final Long menuId, final long quantity, final String menuName, final BigDecimal price) {
        this(null, menuId, new Quantity(quantity), new Name(menuName), new Price(price));
    }

    private OrderLineItem(final Long seq, final Long menuId, final Quantity quantity, final Name menuName, final Price price) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
        this.menuName = menuName;
        this.price = price;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity.getValue();
    }

    public String getMenuName() {
        return menuName.getValue();
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }
}
