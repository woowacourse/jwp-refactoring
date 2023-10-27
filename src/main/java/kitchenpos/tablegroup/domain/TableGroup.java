package kitchenpos.tablegroup.domain;

import kitchenpos.tablegroup.domain.validator.TableGroupValidator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    private TableGroup(final LocalDateTime createdDate) {
        this(null, createdDate);
    }

    private TableGroup(final Long id,
                      final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroup create(final List<Long> orderTableIds,
                                    final TableGroupValidator validator) {
        validator.validateOrderTable(orderTableIds);
        return new TableGroup(LocalDateTime.now());
    }

    public void ungroup(final GroupingService groupingService,
                        final TableGroupValidator validator) {
        validator.validateCompletedOrderTableInTableGroup(id);
        groupingService.ungroup(id);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TableGroup{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                '}';
    }
}
