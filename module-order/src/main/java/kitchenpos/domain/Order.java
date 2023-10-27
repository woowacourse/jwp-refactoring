package kitchenpos.domain;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(STRING)
    private OrderStatus orderStatus;

    private long orderTableId;

    @CreatedDate
    private LocalDateTime orderedTime;

    protected Order() {
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus) {
        this(null, orderTable, orderStatus);
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus) {
        checkOrderTableNotEmpty(orderTable);
        this.id = id;
        this.orderTableId = orderTable.id();
        this.orderStatus = orderStatus;
    }

    private void checkOrderTableNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalStateException("테이블이 주문 불가능한 상태입니다.");
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new IllegalStateException("이미 계산이 끝난 상태입니다.");
        }
        this.orderStatus = orderStatus;
    }

    public Long id() {
        return id;
    }

    public OrderStatus orderStatus() {
        return orderStatus;
    }

    public long orderTableId() {
        return orderTableId;
    }

    public LocalDateTime orderedTime() {
        return orderedTime;
    }
}
