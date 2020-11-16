package kitchenpos.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.exception.AlreadyEmptyTableException;
import kitchenpos.exception.AlreadyInTableGroupException;
import kitchenpos.exception.NegativeNumberOfGuestsException;
import kitchenpos.exception.TableGroupWithNotEmptyTableException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
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

    public void addToTableGroup(Long tableGroupId) {
        if (!this.empty) {
            throw new TableGroupWithNotEmptyTableException(this.id);
        }
        if (Objects.nonNull(this.tableGroupId)) {
            throw new AlreadyInTableGroupException(this.id, this.tableGroupId);
        }
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroupId = null;
        this.empty = false;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new AlreadyInTableGroupException(this.id, this.tableGroupId);
        }

        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new NegativeNumberOfGuestsException(this.id);
        }

        if (empty) {
            throw new AlreadyEmptyTableException(this.id);
        }

        this.numberOfGuests = numberOfGuests;
    }
}
