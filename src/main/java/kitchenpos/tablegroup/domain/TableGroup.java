package kitchenpos.tablegroup.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    public TableGroup() {
        this(null);
    }

    public TableGroup(Long id) {
        this.id = id;
    }

    public Long id() {
        return id;
    }

    public LocalDateTime createdDate() {
        return createdDate;
    }
}
