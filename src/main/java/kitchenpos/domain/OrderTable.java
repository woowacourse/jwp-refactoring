package kitchenpos.domain;

import java.util.Objects;

public class OrderTable {

    private final Long id;
    private Long tableGroupId;
    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTable(Long id, Long tableGroupId, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = Objects.requireNonNullElse(numberOfGuests, 0);
        this.empty = Objects.requireNonNullElse(empty, false);
    }

    public OrderTable(Integer numberOfGuests, Boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public boolean isEmpty() {
        return empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }
}
