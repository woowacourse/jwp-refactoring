package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class OrderTableLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(nullable = false)
    private Order order;

    @Column(nullable = false)
    private Long orderTableId;

    @Column(nullable = false)
    private int numberOfGuests;

    protected OrderTableLog() {
    }

    public OrderTableLog(Order order, Long orderTableId, int numberOfGuests) {
        this(null, order, orderTableId, numberOfGuests);
    }

    public OrderTableLog(Long id, Order order, Long orderTableId, int numberOfGuests) {
        this.id = id;
        this.order = order;
        this.orderTableId = orderTableId;
        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
