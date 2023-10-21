package kitchenpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.domain.exception.OrderTableException.EmptyTableException;
import kitchenpos.domain.exception.OrderTableException.ExistsTableGroupException;
import kitchenpos.domain.exception.OrderTableException.InvalidNumberOfGuestsException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIgnore
    private TableGroup tableGroup;
    @Column
    private int numberOfGuests;
    @Column
    private boolean empty = true;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(final boolean empty) {
        if (tableGroup != null) {
            throw new ExistsTableGroupException();
        }
        this.empty = empty;
    }

    public void changeNumberOfGuest(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new InvalidNumberOfGuestsException(numberOfGuests);
        }
        if (empty) {
            throw new EmptyTableException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
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
