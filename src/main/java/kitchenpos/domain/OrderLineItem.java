package kitchenpos.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_ID")
    private Menu menu;

    private String originalMenuName;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "original_menu_price"))
    private Price originalMenuPrice;

    private Long originalMenuGroupId;

    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(final Menu menu, final long quantity) {
        this.menu = menu;
        this.originalMenuName = menu.getName();
        this.originalMenuPrice = menu.getPrice();
        this.originalMenuGroupId = menu.getMenuGroup().getId();
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getOriginalMenuName() {
        return originalMenuName;
    }

    public Price getOriginalMenuPrice() {
        return originalMenuPrice;
    }

    public Long getOriginalMenuGroupId() {
        return originalMenuGroupId;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }
}
