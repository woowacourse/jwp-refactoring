package kitchenpos.domain.ordertable;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.common.NumberOfGuests;
import kitchenpos.domain.exception.InvalidEmptyOrderTableException;

@Entity
public class OrderTable {

    private static final Long UNGROUP_TABLE_GROUP = null;
    private static final boolean UNGROUP_EMPTY = true;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void group(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        this.tableGroupId = UNGROUP_TABLE_GROUP;
        this.empty = UNGROUP_EMPTY;
    }

    public boolean isUngrouping() {
        return tableGroupId == null;
    }

    public void changeEmptyStatus(final boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (this.empty) {
            throw new InvalidEmptyOrderTableException();
        }

        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(final Object target) {
        if (this == target) {
            return true;
        }
        if (target == null || getClass() != target.getClass()) {
            return false;
        }

        final OrderTable targetOrderTable = (OrderTable) target;

        return Objects.equals(getId(), targetOrderTable.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
