package kitchenpos.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.exception.OrderTableExceptionType.ILLEGAL_CHANGE_NUMBER_OF_GUESTS;
import static kitchenpos.exception.OrderTableExceptionType.NUMBER_OF_GUESTS_IS_NULL_OR_NEGATIVE_EXCEPTION;
import static kitchenpos.exception.OrderTableExceptionType.TABLE_GROUP_IS_NOT_NULL_EXCEPTION;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.exception.OrderTableException;

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

    protected OrderTable() {
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        if (tableGroup != null) {
            throw new OrderTableException(TABLE_GROUP_IS_NOT_NULL_EXCEPTION);
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(Integer numberOfGuests) {
        if (numberOfGuests == null || numberOfGuests < 0) {
            throw new OrderTableException(NUMBER_OF_GUESTS_IS_NULL_OR_NEGATIVE_EXCEPTION);
        }
        if (isEmpty()) {
            throw new OrderTableException(ILLEGAL_CHANGE_NUMBER_OF_GUESTS);
        }

        this.numberOfGuests = numberOfGuests;
    }

    public boolean hasTableGroup() {
        return tableGroup != null;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        tableGroup.ungroup(this);
        this.tableGroup = null;
        this.empty = false;
    }

    public Long id() {
        return id;
    }

    public TableGroup tableGroup() {
        return tableGroup;
    }

    public int numberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
