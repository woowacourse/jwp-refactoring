package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);

    @PrePersist
    private void prePersist() {
        createdDate = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    }

    public TableGroup() {}

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
