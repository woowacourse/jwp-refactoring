package kitchenpos.table.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime createdDate;

    public TableGroup() {
    }

    public TableGroup(LocalDateTime localDateTime) {
        this(null, localDateTime);
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroup create(LocalDateTime localDateTime) {
        return new TableGroup(localDateTime);
    }

    public static TableGroup create(Long id, LocalDateTime localDateTime) {
        return new TableGroup(id, localDateTime);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
