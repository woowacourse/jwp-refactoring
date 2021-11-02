package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
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

    @Embedded
    private OrderTableGroup orderTables;

    protected TableGroup() {}

    public static TableGroup create(List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.orderTables = OrderTableGroup.create(orderTables);
        tableGroup.createdDate = LocalDateTime.now();
        return tableGroup;
    }

    public static TableGroup createSingleTables(List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.orderTables = OrderTableGroup.createSingleTables(orderTables);
        tableGroup.createdDate = LocalDateTime.now();
        return tableGroup;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.values();
    }
}
