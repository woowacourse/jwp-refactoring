package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime createdDate;
    @Transient
    private GroupTables groupTables;

    protected TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        this(null, null, new GroupTables(orderTables));
    }

    private TableGroup(Long id, LocalDateTime createdDate, GroupTables groupTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.groupTables = groupTables;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public GroupTables getGroupTables() {
        return groupTables;
    }

    public void register() {
        if (!groupTables.canAssignTableGroup()) {
            throw new IllegalArgumentException();
        }
        groupTables.setTableGroup(this);
        createdDate = LocalDateTime.now();
    }
}
