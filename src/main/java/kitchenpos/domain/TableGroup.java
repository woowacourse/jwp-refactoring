package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
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

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<Table> tables = new ArrayList<>();

    public TableGroup() {
    }

    protected TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    private TableGroup(Long id, LocalDateTime createdDate,
        List<Table> tables) {
        this.id = id;
        this.createdDate = createdDate;
        this.tables = tables;
        setTableGroup(tables);
    }

    public static TableGroup entityOf(List<Table> tables) {
        return new TableGroup(null, LocalDateTime.now(), tables);
    }

    private void setTableGroup(List<Table> tables) {
        for (Table table : tables) {
            if (!table.isEmpty()) {
                throw new IllegalArgumentException("Table이 비어있지 않으므로 TableGroup을 설정할 수 없습니다.");
            }
            table.setTableGroup(this);
            table.setEmpty(false);
        }
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

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(final List<Table> tables) {
        this.tables = tables;
    }

    public void addTables(Table table) {
        if (!tables.contains(table)) {
            this.tables.add(table);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableGroup that = (TableGroup) o;
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
            ", orderTables=" + tables +
            '}';
    }
}
