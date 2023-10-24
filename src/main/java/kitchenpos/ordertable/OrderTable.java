package kitchenpos.ordertable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    @Embedded
    private NumberOfGuests numberOfGuests;
    @Embedded
    private Empty empty;

    protected OrderTable() {
    }

    public OrderTable(final Long tableGroupId, final NumberOfGuests numberOfGuests, final Empty empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(final NumberOfGuests numberOfGuests, final Empty empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final Long id,
                      final Long tableGroupId,
                      final NumberOfGuests numberOfGuests,
                      final Empty empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty.getEmpty();
    }

    public void updateTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void updateNumberOfGuests(final NumberOfGuests count) {
        this.numberOfGuests = count;
    }

    public void updateEmpty(final Empty empty) {
        this.empty = empty;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
