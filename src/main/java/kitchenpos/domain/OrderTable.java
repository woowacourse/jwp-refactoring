package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.exception.badrequest.OrderTableAlreadyInGroupException;
import kitchenpos.exception.badrequest.OrderTableNegativeNumberOfGuestsException;
import kitchenpos.exception.badrequest.OrderTableUnableToChangeNumberOfGuestsWhenEmptyException;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "table_group_id")
    private Long tableGroupId;
    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;
    @Column(name = "empty", nullable = false)
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;

        validateZeroOrPositive(this.numberOfGuests);
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    private void validateZeroOrPositive(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new OrderTableNegativeNumberOfGuestsException(this.numberOfGuests);
        }
    }

    public OrderTable changeEmpty(final boolean empty) {
        validateNotInGroup();
        this.empty = empty;

        return this;
    }

    private void validateNotInGroup() {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new OrderTableAlreadyInGroupException(this.tableGroupId);
        }
    }

    public OrderTable changeNumberOfGuests(final int numberOfGuests) {
        validateZeroOrPositive(numberOfGuests);
        validateIsNotEmpty();
        this.numberOfGuests = numberOfGuests;

        return this;
    }

    private void validateIsNotEmpty() {
        if (this.empty) {
            throw new OrderTableUnableToChangeNumberOfGuestsWhenEmptyException();
        }
    }

    public void changeTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
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
