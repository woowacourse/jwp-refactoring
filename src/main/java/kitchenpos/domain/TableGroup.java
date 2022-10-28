package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import kitchenpos.application.event.TableGroupEvent;
import kitchenpos.application.event.TableUnGroupEvent;
import kitchenpos.domain.validator.TableGroupValidator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup extends AbstractAggregateRoot<TableGroup> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint(20)")
    private Long id;
    @CreatedDate
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime createdDate;
    @Transient
    private Set<Long> orderTableIds = new HashSet<>();
    @Transient
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final Set<Long> orderTableIds) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableIds = orderTableIds;
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(final Set<Long> orderTableIds) {
        this(null, null, orderTableIds);
    }

    public void group(final TableGroupValidator tableGroupValidator) {
        tableGroupValidator.validateGroup(this);

        registerEvent(new TableGroupEvent(this));
    }

    public TableGroup unGroup(final TableGroupValidator tableGroupValidator) {
        tableGroupValidator.validateUnGroup(this);

        registerEvent(new TableUnGroupEvent(this.id));

        return this;
    }

    public TableGroup addOrderTables(final List<OrderTable> groupedTables) {
        orderTables.addAll(groupedTables);
        return this;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public Set<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
