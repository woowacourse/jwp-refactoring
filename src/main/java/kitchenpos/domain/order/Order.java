package kitchenpos.domain.order;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id", nullable = false)
    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @CreatedDate
    @Column(name = "ordered_time", nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    public Order(final Long id, final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                 final OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(final Long orderTableId, final OrderLineItems orderLineItems) {
        this(null, orderTableId, null, null, orderLineItems);
    }

    public Order(final Long orderTableId, final OrderStatus orderStatus) {
        this(null, orderTableId, orderStatus, LocalDateTime.now(), null);
    }

    public void updateOrderLineItems(final OrderLineItems orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems.getValue())) {
            throw new IllegalArgumentException("OrderLineItem이 존재하지 않습니다.");
        }
        this.orderLineItems = orderLineItems;
    }

    public void changeStatus(final OrderStatus orderStatus) {
        if (this.orderStatus.isCompleted()) {
            throw new IllegalStateException("이미 완료된 Order의 상태는 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus;
    }
}
