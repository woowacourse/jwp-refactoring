package kitchenpos.ordertable.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String orderStatus;

    @Column(name = "order_table_id")
    private Long orderTableId;

    private LocalDateTime orderedTime;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(Long id, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = makeOrderItems(orderLineItems);
    }

    public Order(Long id, String orderStatus, Long orderTableId, LocalDateTime orderedTime,
                 List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderTableId = orderTableId;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    private List<OrderLineItem> makeOrderItems(List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null) {
            return new ArrayList<>();
        }
        return orderLineItems;
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        for (OrderLineItem orderLineItem : orderLineItems) {
            addOrderLineItem(orderLineItem);
        }
    }

    private void addOrderLineItem(OrderLineItem orderLineItem) {
        validate(orderLineItem);
        this.orderLineItems.add(orderLineItem);
    }

    private void validate(OrderLineItem orderLineItem) {
        alreadyExistMenu(orderLineItem);
        anotherOrderMenu(orderLineItem);
    }

    private void alreadyExistMenu(OrderLineItem orderLineItem) {
        orderLineItems.stream()
            .filter(originItem -> originItem.getMenuId() == orderLineItem.getMenuId())
            .findAny()
            .ifPresent(ignore -> {
                throw new IllegalArgumentException("주문에 이미 해당 메뉴가 포함되어 있습니다");
            });
    }

    private void anotherOrderMenu(OrderLineItem orderLineItem) {
        if (orderLineItem.getOrderId() != null) {
            throw new IllegalArgumentException("다른 주문의 상품입니다.");
        }
    }

    public void changeStatus(OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), this.orderStatus)) {
            throw new IllegalArgumentException("완료된 주문은 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus.name();
    }

    public Long getId() {
        return id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
