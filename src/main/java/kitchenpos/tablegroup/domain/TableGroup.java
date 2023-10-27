package kitchenpos.tablegroup.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.BaseEntity;

@Entity
public class TableGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public TableGroup() {
    }

    public Long getId() {
        return id;
    }
}
