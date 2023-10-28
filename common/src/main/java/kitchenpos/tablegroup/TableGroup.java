package kitchenpos.tablegroup;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import suppoert.domain.BaseEntity;

@Entity
public class TableGroup extends BaseEntity {

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
