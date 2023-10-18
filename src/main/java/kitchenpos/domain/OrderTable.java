package kitchenpos.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.domain.exception.OrderTableExceptionType.NUMBER_OF_GUEST_LOWER_THAN_ZERO;
import static kitchenpos.domain.exception.OrderTableExceptionType.TABLE_CANT_CHANGE_EMPTY_ALREADY_IN_GROUP;
import static kitchenpos.domain.exception.OrderTableExceptionType.TABLE_CANT_CHANGE_NUMBER_OF_GUESTS_EMPTY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.exception.OrderTableException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        validateNumberOfGuest(numberOfGuests);
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    protected OrderTable() {

    }

    private void validateNumberOfGuest(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new OrderTableException(NUMBER_OF_GUEST_LOWER_THAN_ZERO);
        }
    }

    public Long getId() {
        return id;
    }

    public void changeTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuest(numberOfGuests);
        if (empty) {
            throw new OrderTableException(TABLE_CANT_CHANGE_NUMBER_OF_GUESTS_EMPTY);
        }
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        if (tableGroup != null) {
            throw new OrderTableException(TABLE_CANT_CHANGE_EMPTY_ALREADY_IN_GROUP);
        }
        this.empty = empty;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
