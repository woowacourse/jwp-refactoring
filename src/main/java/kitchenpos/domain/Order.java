package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
    @CreatedDate
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();
    @Version
    private int version;

    @Builder
    public Order(
        final OrderTable orderTable,
        final OrderStatus orderStatus,
        final List<OrderLineItem> orderLineItems
    ) {
        setOrderTable(orderTable);
        this.orderStatus = orderStatus;
        setOrderLineItems(orderLineItems);
    }

    private void setOrderTable(final OrderTable orderTable) {
        validateOrderTable(orderTable);
        if (Objects.isNull(this.orderTable)) {
            this.orderTable = orderTable;
            this.orderTable.addOrder(this);
        }
    }

    private void validateOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        orderLineItems.forEach(this::addOrderLineItem);
    }

    private void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    private void addOrderLineItem(final OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
        orderLineItem.setOrder(this);
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        validateOrderStatus();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatus() {
        if (orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return new ArrayList<>(orderLineItems);
    }
}
