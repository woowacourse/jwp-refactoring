package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST})
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    @CreatedDate
    private LocalDateTime orderedTime;

    protected Order() {
    }

    private Order(OrderTable orderTable, OrderStatus orderStatus) {
        this(null, orderTable, orderStatus);
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus) {
        validate(orderTable);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    public static Order init(OrderTable orderTable) {
        return new Order(orderTable, OrderStatus.COOKING);
    }

    private void validate(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 주문 테이블입니다.");
        }
    }

    public void setUpOrderLineItems(List<OrderLineItem> orderLineItems) {
        validateSize(orderLineItems);
        this.orderLineItems.addAll(orderLineItems);
    }

    private void validateSize(List<OrderLineItem> orderLineItems) {
        long menuSize = orderLineItems.stream()
            .map(orderLineItem -> orderLineItem.getMenu().getId())
            .distinct()
            .count();
        if (menuSize != orderLineItems.size()) {
            throw new IllegalArgumentException("올바르지 않은 메뉴입니다.");
        }
    }

    public void changeStatus(OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException("이미 계산 완료된 주문입니다.");
        }
        this.orderStatus = orderStatus;
    }

    public boolean isAbleToUngroup() {
        return orderStatus == OrderStatus.COMPLETION;
    }

    public boolean isAbleToChangeEmpty() {
        return orderStatus == OrderStatus.COMPLETION;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
