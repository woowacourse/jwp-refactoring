package domain;

import static domain.OrderStatus.COOKING;

import exception.OrderException;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    public Order() {
        this(null, COOKING, LocalDateTime.now());
    }

    public Order(Long id, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public void changeOrderStatus(String orderStatus) {
        OrderStatus.checkIfHas(orderStatus);

        if (OrderStatus.checkWhetherCompletion(this.orderStatus)) {
            throw new OrderException("이미 완료된 주문입니다.");
        }

        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    public void validateOrderStatusIsCompletion() {
        if (OrderStatus.checkWhetherMeal(orderStatus) || OrderStatus.checkWhetherCooking(orderStatus)) {
            throw new OrderException("주문 상태가 주문 완료가 아닙니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
