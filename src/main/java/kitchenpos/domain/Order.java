package kitchenpos.domain;

import kitchenpos.common.BaseCreateTimeEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "order_order_table"))
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(final OrderTable orderTable, final OrderStatus orderStatus) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에는 주문을 생성할 수 없습니다.");
        }

        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    public void addOrderLineItems(final List<OrderLineItem> orderLineItems) {
        for (final OrderLineItem orderLineItem : orderLineItems) {
            this.orderLineItems.add(orderLineItem);
            orderLineItem.setOrder(this);
        }
    }

    public void changeOrderStatus(final OrderStatus newOrderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException("완료된 주문의 상태를 변경할 수 없습니다.");
        }

        this.orderStatus = newOrderStatus;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
