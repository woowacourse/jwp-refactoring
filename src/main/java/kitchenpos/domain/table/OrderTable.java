package kitchenpos.domain.table;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final int numberOfGuests) {
        this(null, numberOfGuests, false);
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
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

    public void updateEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void ungroup() {
        tableGroupId = null;
        empty = false;
    }

    public Long getTableGroupIdOrElseNull() {
        if (tableGroupId == null) {
            return null;
        }

        return tableGroupId;
    }

    public void validateCanGroup() {
        if (!empty || Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public void validateEmptyUpdatable() {
        if (tableGroupId != null) {
            throw new IllegalArgumentException();
        }
    }

    public void updateToUsed() {
        empty = false;
    }

    public void validateNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderTable)) {
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
