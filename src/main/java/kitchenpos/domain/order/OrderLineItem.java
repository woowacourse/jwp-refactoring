package kitchenpos.domain.order;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.domain.Price;

@Entity
public class OrderLineItem {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;

    @ManyToOne(optional = false)
    private Order order;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Embedded
    private OrderLineItemName name;

    @Embedded
    private Price price;

    @Embedded
    private OrderLineItemQuantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long menuId, final Long quantity, final String menuName, final BigDecimal menuPrice) {
        this(null, menuId, quantity, menuName, menuPrice);
    }

    public OrderLineItem(final Order order, final Long menuId, final Long quantity, final String menuName, final BigDecimal menuPrice) {
        this(null, order, menuId, quantity, menuName, menuPrice);
    }

    public OrderLineItem(final Long seq, final Order order, final Long menuId, final Long quantity, final String menuName, final BigDecimal menuPrice) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = new OrderLineItemQuantity(quantity);
        this.name = new OrderLineItemName(menuName);
        this.price = new Price(menuPrice);
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity.getValue();
    }

    public String getName() {
        return name.getValue();
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public void setOrder(final Order order) {
        this.order = order;
    }
}
