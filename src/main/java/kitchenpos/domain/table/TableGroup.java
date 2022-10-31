package kitchenpos.domain.table;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "table_group")
@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    public TableGroup() {
        this(null, LocalDateTime.now(), null);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this(id, createdDate, null);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
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

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
