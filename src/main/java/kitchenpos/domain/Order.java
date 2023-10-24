package kitchenpos.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    private OrderTable orderTable;

    private String orderStatus;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    private Order(final OrderTable orderTable, final String orderStatus) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    private void addOrderLineItem(final OrderLineItem orderLineItem) {
        if (Objects.isNull(orderLineItems)) {
            orderLineItems = new ArrayList<>();
        }
        orderLineItems.add(orderLineItem);
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void changeOrderStatus(final OrderStatus status) {
        if (OrderStatus.COMPLETION.name().equals(orderStatus)) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = status.name();
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public static class OrderFactory {

        private static final String DEFAULT_STATUS = OrderStatus.COOKING.name();

        private Order order;

        public OrderFactory(final OrderTable orderTable) {
            if (orderTable.isEmpty()) {
                throw new IllegalArgumentException();
            }

            this.order = new Order(orderTable, DEFAULT_STATUS);
        }

        public OrderFactory addMenu(final Menu menu, final long quantity) {
            order.addOrderLineItem(new OrderLineItem(order, menu, quantity));
            return this;
        }

        public Order create() {
            if (Objects.isNull(order)) {
                throw new IllegalStateException("Order is already created with this factory " + this);
            }
            if (CollectionUtils.isEmpty(order.orderLineItems)) {
                throw new IllegalArgumentException();
            }
            final Order result = order;
            order = null;

            return result;
        }
    }
}
