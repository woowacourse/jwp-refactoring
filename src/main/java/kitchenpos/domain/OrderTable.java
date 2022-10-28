package kitchenpos.domain;

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

    private Long tableGroupId;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        validateNumberOfGuestsNegative(numberOfGuests);
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void group(final Long tableGroupId) {
        changeTableGroupId(tableGroupId);
        changeEmpty(false);
    }

    public void ungroup() {
        changeTableGroupId(null);
        changeEmpty(false);
    }

    private void validateNumberOfGuestsNegative(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 인원은 음수가 될 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void changeTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuestsNegative(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }
}
