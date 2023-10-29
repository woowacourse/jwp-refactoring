package kitchenpos.tablegroup.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup extends AbstractAggregateRoot<TableGroup>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdTime;

    public TableGroup() {
    }

    public void group(final List<Long> orderTableIds) {
        validateTableCount(orderTableIds);

        final GroupTableEvent groupEvent = new GroupTableEvent(this, orderTableIds);

        registerEvent(groupEvent);
    }

    private void validateTableCount(final List<Long> orderTableIds) {
        if (orderTableIds.isEmpty() || orderTableIds.size() < 2) {
            throw new IllegalArgumentException("그룹화하려는 테이블은 2개 이상이어야 합니다.");
        }
    }

    public void ungroup() {
        final UngroupTableEvent ungroupTableEvent = new UngroupTableEvent(this);

        registerEvent(ungroupTableEvent);
    }

    public Long getId() {
        return id;
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
}
