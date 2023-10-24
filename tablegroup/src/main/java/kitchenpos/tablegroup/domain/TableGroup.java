package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;


public class TableGroup {
    @Id
    private Long id;
    private LocalDateTime createdDate;


    private TableGroup() {
    }

    private TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroup create() {
        return new TableGroup(null, LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
