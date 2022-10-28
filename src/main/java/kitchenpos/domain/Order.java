package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Column
    private String orderStatus;

    @Column
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(OrderTable orderTable, Map<Menu, Long> orderLineItems) {
        validate(orderTable, orderLineItems);

        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING.name();
        this.orderedTime = LocalDateTime.now();

        for (Menu menu : orderLineItems.keySet()) {
            final OrderLineItem orderLineItem = new OrderLineItem(this, menu, orderLineItems.get(menu));
            this.orderLineItems.add(orderLineItem);
        }
    }

    public void changeStatus(String status) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), orderStatus)) {
            throw new IllegalArgumentException("이미 완료된 주문 상태를 변경할 수 없습니다.");
        }

        orderStatus = status;
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

    private void validate(OrderTable orderTable, Map<Menu, Long> orderLineItems) {
        validateEmptyOrderTable(orderTable);
        validateOrderLineItems(orderLineItems);
    }

    private void validateEmptyOrderTable(OrderTable orderTable) {
        if (orderTable == null) {
            throw new IllegalArgumentException("주문 테이블은 비어있을 수 없습니다.");
        }
    }

    private void validateOrderLineItems(Map<Menu, Long> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목은 비어있을 수 없습니다.");
        }
    }
}
