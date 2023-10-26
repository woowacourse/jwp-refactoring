package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    private String orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
                 List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = makeOrderItems(orderLineItems);
    }

    private List<OrderLineItem> makeOrderItems(List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null) {
            return new ArrayList<>();
        }
        return orderLineItems.stream()
            .map(orderLineItem -> orderLineItem.assignOrder(this))
            .collect(Collectors.toList());
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
        invalidOrderId(orderLineItem);
        alreadyExistMenu(orderLineItem);
    }

    private void invalidOrderId(OrderLineItem orderLineItem) {
        if (orderLineItem.getOrder().getId() != id) {
            throw new IllegalArgumentException("추가한 주문 상품은 해당 주문의 상품이 아닙니다");
        }
    }

    private void alreadyExistMenu(OrderLineItem orderLineItem) {
        orderLineItems.stream()
            .filter(originItem -> originItem.getMenuId() == orderLineItem.getMenuId())
            .findAny()
            .ifPresent(ignore -> {
                throw new IllegalArgumentException("주문에 이미 해당 메뉴가 포함되어 있습니다");
            });
    }

    public void tabling(OrderTable orderTable) {
        this.orderTableId = orderTable.getId();
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

    public Long getOrderTableId() {
        return orderTableId;
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
