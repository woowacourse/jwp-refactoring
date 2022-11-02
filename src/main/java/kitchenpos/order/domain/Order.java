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

    public Order(
        final Long orderTableId,
        final List<OrderLineItemDto> orderLineItems,
        final OrderValidator orderValidator) {
        orderValidator.validateCreateOrder(orderTableId, orderLineItems);

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

    public void changeStatus(String status, OrderValidator orderValidator) {
        orderValidator.validateChangeStatus(this);
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
