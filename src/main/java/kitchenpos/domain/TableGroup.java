package kitchenpos.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TableGroup extends BaseEntity {
    public static final int MIN_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public TableGroup() {
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        super(createdDate);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
