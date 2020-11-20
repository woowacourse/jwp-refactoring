package kitchenpos.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.exception.AlreadyCompleteOrderException;

@Entity
@Table(name = "Orders")
public class Order extends BaseEntity {
    public static final OrderStatus INITIAL_STATUS = OrderStatus.COOKING;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Order() {
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public static Order of(Long id, Long orderTableId, String orderStatus) {
        return new Order(id, orderTableId, OrderStatus.valueOf(orderStatus));
    }

    public static Order of(Long orderTableId, OrderStatus orderStatus) {
        if (orderStatus != INITIAL_STATUS) {
            throw new IllegalArgumentException();
        }

        return new Order(null, orderTableId, orderStatus);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void changeOrderStatus(String orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new AlreadyCompleteOrderException(this.id);
        }
        this.orderStatus = OrderStatus.valueOf(orderStatus.toUpperCase());
    }
}
