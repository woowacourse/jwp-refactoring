package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        if (empty) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void group(Long tableGroupId) {
        validateTableToGroup();
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    private void validateTableToGroup() {
        if (!empty || Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public void unGroup() {
        this.tableGroupId = null;
        this.empty = false;
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
