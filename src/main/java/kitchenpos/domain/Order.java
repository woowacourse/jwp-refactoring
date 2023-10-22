package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        this.orderLineItems = makeList(orderLineItems);
    }

    private List<OrderLineItem> makeList(List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(orderLineItems);
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
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

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        for (OrderLineItem orderLineItem : orderLineItems) {
            addOrderLineItem(orderLineItem);
        }
    }

    public void tabling(OrderTable orderTable) {
        this.orderTableId = orderTable.getId();
    }

    public void changeStatus(OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), this.orderStatus)) {
            throw new IllegalArgumentException();
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
