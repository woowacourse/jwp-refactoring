package kitchenpos.domain.tablegroup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import kitchenpos.domain.base.BaseIdEntity;
import kitchenpos.domain.table.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup extends BaseIdEntity {

    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<Table> tables = new ArrayList<>();

    protected TableGroup() {
    }

    private TableGroup(Long id, LocalDateTime createdDate, List<Table> tables) {
        super(id);
        validate(tables);

        this.createdDate = createdDate;
        this.tables = tables;
        setTableGroup(tables);
    }

    public static TableGroup of(Long id, LocalDateTime createdDate, List<Table> tables) {
        return new TableGroup(id, createdDate, tables);
    }

    public static TableGroup entityOf(List<Table> tables) {
        return new TableGroup(null, LocalDateTime.now(), tables);
    }

    private static void validate(List<Table> tables) {
        if (CollectionUtils.isEmpty(tables) || tables.size() < 2) {
            throw new IllegalArgumentException("테이블은 2개 이상이어야 합니다.");
        }
    }

    private void setTableGroup(List<Table> tables) {
        for (Table table : tables) {
            validateTableEmpty(table);
            table.setTableGroup(this);
            table.setEmpty(false);
        }
    }

    private void validateTableEmpty(Table table) {
        if (!table.isEmpty()) {
            throw new IllegalArgumentException("Table이 비어있지 않으므로 TableGroup을 설정할 수 없습니다.");
        }
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void addTables(Table table) {
        if (!tables.contains(table)) {
            this.tables.add(table);
        }
    }

    @Override
    public String toString() {
        return "TableGroup{" +
            "id=" + getId() +
            ", createdDate=" + createdDate +
            ", orderTables=" + tables +
            '}';
    }
}
