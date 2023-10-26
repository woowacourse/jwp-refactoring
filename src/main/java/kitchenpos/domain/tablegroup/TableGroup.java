package kitchenpos.domain.tablegroup;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    public TableGroup() {
    }

    public TableGroup(final LocalDateTime createdDate) {
        this(null, createdDate);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroup createWithNowCreatedDate() {
        return new TableGroup(LocalDateTime.now());
    }

    public Long id() {
        return id;
    }

    public LocalDateTime createdDate() {
        return createdDate;
    }
}
