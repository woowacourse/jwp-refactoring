package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @Embedded
    private GroupedTables groupedTables;

    public TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        this(null, LocalDateTime.now(), orderTables);
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.groupedTables = new GroupedTables(orderTables);
    }

    public void bindTablesToGroup() {
        this.groupedTables.bindTablesToGroup(id);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return groupedTables.getOrderTables();
    }

    public GroupedTables getGroupedTables() {
        return groupedTables;
    }
}
