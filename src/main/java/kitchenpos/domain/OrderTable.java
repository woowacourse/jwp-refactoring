package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long id) {
        this(id, null, 0, false);
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void addTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void changeSingleEmpty(final boolean empty) {
        if (Objects.nonNull(this.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
        this.empty = empty;
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNotEmpty();
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public void validateNotEmpty() {
        if (this.empty) {
            throw new IllegalArgumentException();
        }
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void validateNotTableGroupId() {
        if (tableGroupId == null) {
            throw new IllegalArgumentException();
        }
    }

    public void validateEmpty() {
        if (!this.empty) {
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
}
