package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        validateNumberOfGuests(numberOfGuests);
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public void validateTableGroupForChangeEmpty( ) {
        if (Objects.nonNull(getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    public void validateEmptyForChangeGuestNumber() {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isNonNullTableGroup() {
        return Objects.nonNull(getTableGroupId());
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }
}
