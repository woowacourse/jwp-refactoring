package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity(name = "orders")
public class Order {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @JoinColumn(name = "order_table_id")
    @ManyToOne
    private OrderTable orderTable;
    private String orderStatus;
    @CreatedDate
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        this(null, orderTable, OrderStatus.COOKING.name(), orderLineItems);
    }

    public Order(final Long id, final OrderTable orderTable, final String orderStatus,
                 final List<OrderLineItem> orderLineItems) {
        validateToConstruct(orderTable, orderLineItems);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
        orderTable.addOrder(this);
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(this);
        }
    }

    private void validateToConstruct(final OrderTable orderTable,
                                     final List<OrderLineItem> orderLineItems) {
        validateOrderTable(orderTable);
        validateOrderLineItemsSize(orderLineItems);
    }

    private void validateOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(String.format(
                "빈 상태의 테이블에는 주문을 추가할 수 없습니다.(table id: %d)",
                orderTable.getId()
            ));
        }
    }

    private void validateOrderLineItemsSize(final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.size() == 0) {
            throw new IllegalArgumentException("주문 항목은 1개이상 입니다.");
        }
    }

    public void changeOrder(final String newOrderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), orderStatus)) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = newOrderStatus;
    }

    public boolean isOrderStatusIn(final List<String> orderStatuses) {
        return orderStatuses.stream()
            .anyMatch(orderStatus -> orderStatus.equals(this.orderStatus));
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
}
