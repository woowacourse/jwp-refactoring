package kitchenpos.domain;

import kitchenpos.domain.exception.InvalidOrderChangeException;
import kitchenpos.domain.exception.InvalidOrderException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Order {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    
    @CreatedDate
    private LocalDateTime orderedTime;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderLineItem> orderLineItems;
    
    public Order(final OrderTable orderTable,
                 final OrderStatus orderStatus,
                 final List<OrderLineItem> orderLineItems) {
        this(null, orderTable, orderStatus, null, orderLineItems);
    }
    
    public Order(final Long id,
                 final OrderTable orderTable,
                 final OrderStatus orderStatus,
                 final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        validate(orderTable, orderLineItems);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        this.orderLineItems.forEach(orderLineItem -> new OrderLineItem(this,
                              orderLineItem.getMenu(),
                              orderLineItem.getQuantity()));
    }
    
    private void validate(final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        validateIfOrderTableIsEmpty(orderTable);
        validateOrderLineItemsEmpty(orderLineItems);
        validateIfDuplicatedMenuInOrderLineItems(orderLineItems);
        
    }
    
    private void validateOrderLineItemsEmpty(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new InvalidOrderException("주문 항목이 없으면 주문할 수 없습니다");
        }
    }
    
    private void validateIfDuplicatedMenuInOrderLineItems(final List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
                                           .map(OrderLineItem::getMenu)
                                           .map(Menu::getId)
                                           .collect(Collectors.toList());
        Set<Long> notDuplicatedMenuIds = new HashSet<>(menuIds);
        if (!notDuplicatedMenuIds.containsAll(menuIds)) {
            throw new InvalidOrderException("같은 메뉴에 대한 주문 항목이 있을 수 없습니다");
        }
    }
    
    private void validateIfOrderTableIsEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new InvalidOrderException("빈 테이블에서는 주문할 수 없습니다");
        }
    }
    
    public void changeOrderStatus(final OrderStatus orderStatus) {
        if(Objects.equals(OrderStatus.COMPLETION.name(),this.orderStatus)) {
            throw new InvalidOrderChangeException("이미 종료된 주문입니다");
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
}
