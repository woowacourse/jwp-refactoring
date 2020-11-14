package kitchenpos.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.exception.AlreadyCompleteOrderException;

@Entity
@Table(name = "Orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    private String orderStatus;

    public Order() {
    }

    public Order(Long id, Long orderTableId, String orderStatus) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public static Order of(Long orderTableId, String orderStatus) {
        return new Order(null, orderTableId, orderStatus);
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

    public void changeOrderStatus(String orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), this.orderStatus)) {
            throw new AlreadyCompleteOrderException(this.id);
        }
        this.orderStatus = orderStatus;
    }
}
