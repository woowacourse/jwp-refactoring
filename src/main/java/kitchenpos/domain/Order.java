package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "orders")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderLineItems orderLineItems;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @CreatedDate
    private LocalDateTime orderedTime;

    protected Order() {
    }

    public Order(final Long id, final OrderStatus orderStatus, final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderLineItems = new OrderLineItems(orderLineItems, this);
    }

    public static Order forSave(final OrderStatus orderStatus, final List<OrderLineItem> orderLineItems) {
        return new Order(null, orderStatus, orderLineItems);
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        validateOrderStatus();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatus() {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException("주문이 완료된 상태는 변경할 수 없습니다.");
        }
    }

    public void joinOrderTable(final OrderTable orderTable) {
        validateOrderTable();
        this.orderTable = orderTable;
    }

    private void validateOrderTable() {
        if (this.orderTable != null) {
            throw new IllegalArgumentException("주문 테이블이 이미 존재합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public boolean isCompleted() {
        return orderStatus == OrderStatus.COMPLETION;
    }
}
