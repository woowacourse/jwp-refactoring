package kitchenpos.order.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

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

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(final Integer numberOfGuests, final Boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public void updateEmpty(boolean isEmpty) {
        this.empty = isEmpty;
    }

    public void updateTableGroup(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public boolean validateTableGroupIsNonNull() {
        return Objects.nonNull(tableGroupId);
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        validateIsEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    private void validateIsEmpty() {
        if (isEmpty()) {
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
}
