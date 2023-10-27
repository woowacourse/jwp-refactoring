package kitchenpos.domain.tablegroup;

import java.time.LocalDateTime;
import java.util.List;

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

    protected TableGroup() {
    }

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroup(final LocalDateTime createdDate) {
        this(null, createdDate);
    }

    public static TableGroup create() {
        return new TableGroup(LocalDateTime.now());
    }

    public void validateGrouping(final List<Long> orderTableIds) {
        if (orderTableIds.isEmpty() || orderTableIds.size() < 2) {
            throw new IllegalArgumentException("orderTables 갯수가 2 이하입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

}
