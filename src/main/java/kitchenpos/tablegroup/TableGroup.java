package kitchenpos.tablegroup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class TableGroup {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column
    private LocalDateTime createdDate;

    public TableGroup() {
        this(null, LocalDateTime.now());
    }

    private TableGroup(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
