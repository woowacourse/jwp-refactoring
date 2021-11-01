package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tableGroup")
    private final List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() {

    }

    public TableGroup(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    public TableGroup(Long id) {
        this.id = id;
    }

    public void addOrderTables(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
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
