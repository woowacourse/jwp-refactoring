package kitchenpos.domain;

import kitchenpos.exception.orderlineitem.AlreadyAssignedOrderLineItemException;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"), nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "kf_order_line_item_menus"), nullable = false)
    private Menu menu;

    @Embedded
    private Quantity quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Menu menu, Long quantity) {
        this.menu = menu;
        this.quantity = Quantity.from(quantity);
    }

    public void belongsTo(Order order) {
        if (this.order != null) {
            throw new AlreadyAssignedOrderLineItemException();
        }
        this.order = order;
    }
}
