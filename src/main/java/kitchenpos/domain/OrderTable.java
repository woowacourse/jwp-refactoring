package kitchenpos.domain;

import javax.persistence.criteria.CriteriaBuilder;

public class OrderTable {
    private final Long id;
    private final Long tableGroupId;
    private final Integer numberOfGuests;
    private final boolean empty;

    public OrderTable(Long id, Long tableGroupId, Integer numberOfGuests, boolean empty) {
        validateNumberOfGuests(numberOfGuests);
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private void validateNumberOfGuests(Integer numberOfGuests) {
        if (numberOfGuests == null || numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public OrderTable(Integer numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable changeEmpty(boolean empty) {
        return new OrderTable(this.id, this.tableGroupId, this.numberOfGuests, empty);
    }

    public OrderTable changeNumberOfGuests(int numberOfGuests) {
        return new OrderTable(this.id, this.tableGroupId, numberOfGuests, this.empty);
    }

    public OrderTable changeTableGroupId(Long tableGroupId) {
        return new OrderTable(this.id, tableGroupId, this.numberOfGuests, false);
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
