package kitchenpos.ordertable.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class OrderTable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests,
                      final boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public OrderTable(final Long tableGroupId,
                      final int numberOfGuests,
                      final boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(final Long id,
                      final Long tableGroupId,
                      final int numberOfGuests,
                      final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void setTableGroup(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuestsOverZero();
        validateActive();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuestsOverZero() {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void validateActive() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    public void detachFromGroup() {
        this.empty = false;
        this.tableGroupId = null;
    }

    public void setEmpty(final boolean empty) {
        validateGrouped();
        this.empty = empty;
    }

    private void validateGrouped() {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderTable{" +
                "id=" + id +
                ", tableGroupId=" + tableGroupId +
                ", numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                '}';
    }
}
