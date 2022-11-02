package kitchenpos.order.domain;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.Hibernate;
import org.springframework.util.CollectionUtils;

import kitchenpos.order.dto.application.OrderLineItemDto;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Column
    private String orderStatus;

    @Column
    private LocalDateTime orderedTime;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "order")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(Long orderTableId, List<OrderLineItemDto> orderLineItems) {
        validate(orderTableId, orderLineItems);

        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING.name();
        this.orderedTime = LocalDateTime.now();

        for (OrderLineItemDto dto : orderLineItems) {
            final OrderLineItem orderLineItem = new OrderLineItem(
                this,
                dto.getMenuName(),
                dto.getMenuPrice(),
                dto.getQuantity()
            );

            this.orderLineItems.add(orderLineItem);
        }
    }

    public void changeStatus(String status) {
        if (OrderStatus.isCompletion(orderStatus)) {
            throw new IllegalArgumentException("이미 완료된 주문 상태를 변경할 수 없습니다.");
        }

        orderStatus = status;
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

    private void validate(Long orderTableId, List<OrderLineItemDto> orderLineItems) {
        validateEmptyOrderTable(orderTableId);
        validateOrderLineItems(orderLineItems);
    }

    private void validateEmptyOrderTable(Long orderTableId) {
        if (orderTableId == null) {
            throw new IllegalArgumentException("주문 테이블은 비어있을 수 없습니다.");
        }
    }

    private void validateOrderLineItems(List<OrderLineItemDto> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목은 비어있을 수 없습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Order order = (Order)o;
        return id != null && Objects.equals(id, order.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
