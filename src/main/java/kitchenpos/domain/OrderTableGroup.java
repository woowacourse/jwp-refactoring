package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class OrderTableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTableGroup() {
    }

    private OrderTableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        this.orderTables.addAll(orderTables);
    }

    public static OrderTableGroup group(List<OrderTable> orderTables) {
        if (orderTables.size() < 2 || orderTables.stream()
                .anyMatch(OrderTable::isTableGrouped)) {
            throw new IllegalArgumentException();
        }
        return new OrderTableGroup(LocalDateTime.now(), orderTables);
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
