package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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

    public OrderTableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
        this.createdDate = createdDate;
        this.orderTables.addAll(orderTables);
        orderTables.forEach(table -> table.group(this));
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
