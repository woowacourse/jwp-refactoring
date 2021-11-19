package kitchenpos.domain.order;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.OrderedMenu;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @Column(nullable = false)
    private long quantity;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Embedded
    private OrderedMenu orderedMenu;

    protected OrderLineItem() {
    }

    public OrderLineItem(Order order, Menu menu, long quantity) {
        this(null, order, menu, quantity);
    }

    public OrderLineItem(Long seq, Order order, Menu menu, long quantity) {
        this(seq, order, menu, quantity, LocalDateTime.now());
    }

    public OrderLineItem(Long seq, Order order, Menu menu, long quantity, LocalDateTime createdAt) {
        this.seq = seq;
        setOrder(order);
        this.menu = menu;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    private void setOrder(Order order) {
        if (this.order != null) {
            this.order.getOrderLineItems().remove(this);
        }
        this.order = order;
        order.getOrderLineItems().add(this);
    }

    public Menu getMenu() {
        if (Objects.nonNull(orderedMenu)) {
            return new Menu(
                    menu.getId(),
                    orderedMenu.getTempMenuName(),
                    orderedMenu.getTempMenuPrice(),
                    menu.getMenuGroup(),
                    menu.getMenuProducts()
            );
        }
        return menu;
    }

    public void updateTemporaryMenu(OrderedMenu orderedMenu) {
        if (Objects.isNull(this.orderedMenu)) {
            this.orderedMenu = orderedMenu;
        }
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
