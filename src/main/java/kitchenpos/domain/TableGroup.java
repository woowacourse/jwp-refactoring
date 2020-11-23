package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.util.CollectionUtils;

public class TableGroup {

    private Long id;
    private LocalDateTime createdDate;
    private List<Table> tables;

    private TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate, List<Table> tables) {
        this(null, createdDate, tables);
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<Table> tables) {
        if (CollectionUtils.isEmpty(tables) || tables.size() < 2) {
            throw new IllegalArgumentException("그룹지을 테이블을 2개 이상 지정해주세요.");
        }
        this.id = id;
        this.createdDate = createdDate;
        this.tables = tables;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }
}
