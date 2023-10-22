package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this(id, createdDate, null);
    }

    public TableGroup(final List<OrderTable> orderTables) {
        this(null, null, orderTables);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        addOrderTables(orderTables);
    }

    private void addOrderTables(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.updateTableGroup(this);
            orderTable.changeEmpty(false);
        }
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
}
