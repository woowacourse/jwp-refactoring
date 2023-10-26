package kitchenpos.domain;

import static java.util.stream.Collectors.toSet;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.exception.ExceptionType.DUPLICATED_ORDER_LINE_ITEM;
import static kitchenpos.exception.ExceptionType.EMPTY_ORDER_TABLE;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.exception.CustomException;
import kitchenpos.exception.ExceptionType;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    @Enumerated(value = STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = {PERSIST, MERGE, REMOVE})
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(
        Long id,
        OrderTable orderTable,
        OrderStatus orderStatus,
        LocalDateTime orderedTime,
        List<OrderLineItem> orderLineItems
    ) {
        validateOrderTable(orderTable);
        validateOrderLineItems(orderLineItems);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        for (OrderLineItem orderLineItem : this.orderLineItems) {
            orderLineItem.setOrder(this);
        }
    }

    private void validateOrderTable(OrderTable orderTable) throws CustomException {
        if (orderTable.isEmpty()) {
            throw new CustomException(EMPTY_ORDER_TABLE);
        }
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        validateNotEmpty(orderLineItems);
        validateUniqueMenu(orderLineItems);
    }

    private void validateNotEmpty(List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null || orderLineItems.isEmpty()) {
            throw new CustomException(ExceptionType.EMPTY_ORDER_LINE_ITEMS);
        }
    }

    private void validateUniqueMenu(List<OrderLineItem> orderLineItems) {
        Set<Menu> uniqueMenus = orderLineItems.stream()
                                              .map(OrderLineItem::getMenu)
                                              .filter(Objects::nonNull)
                                              .collect(toSet());

        if (uniqueMenus.size() != orderLineItems.size()) {
            throw new CustomException(DUPLICATED_ORDER_LINE_ITEM);
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new CustomException(ExceptionType.ALREADY_COMPLETION_ORDER);
        }
        this.orderStatus = orderStatus;
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

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public static class Builder {

        private Long id;
        private OrderTable orderTable;
        private OrderStatus orderStatus;
        private LocalDateTime orderedTime;
        private List<OrderLineItem> orderLineItems;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder orderTable(OrderTable orderTable) {
            this.orderTable = orderTable;
            return this;
        }

        public Builder orderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder orderedTime(LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public Builder orderLineItems(List<OrderLineItem> orderLineItems) {
            this.orderLineItems = orderLineItems;
            return this;
        }

        public Order build() {
            return new Order(id, orderTable, orderStatus, orderedTime, orderLineItems);
        }
    }
}
