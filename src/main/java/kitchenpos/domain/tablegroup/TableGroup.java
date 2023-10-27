package kitchenpos.domain.tablegroup;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.common.CreatedTimeEntity;

@Entity
public class TableGroup extends CreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(final Object target) {
        if (this == target) {
            return true;
        }
        if (target == null || getClass() != target.getClass()) {
            return false;
        }
        final TableGroup targetTableGroup = (TableGroup) target;
        return Objects.equals(getId(), targetTableGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
