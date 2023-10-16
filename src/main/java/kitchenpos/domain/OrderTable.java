package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.exception.NumberOfGuestIsNotPositiveException;
import kitchenpos.exception.OrderTableEmptyException;
import kitchenpos.exception.OrderTableNotEmptyException;
import kitchenpos.exception.TableGroupExistsException;

@Table(name = "order_table")
@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void changeEmpty(boolean empty) {
        validateTableGroupNotExists();
        this.empty = empty;
    }

    public void validateTableGroupNotExists() {
        if (tableGroup != null) {
            throw new TableGroupExistsException();
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateIsNotEmpty();
        validateNumberOfGuestsIsPositive(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public void validateIsNotEmpty() {
        if (isEmpty()) {
            throw new OrderTableEmptyException();
        }
    }

    private void validateNumberOfGuestsIsPositive(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new NumberOfGuestIsNotPositiveException();
        }
    }

    public void validateIsEmpty() {
        if (!isEmpty()) {
            throw new OrderTableNotEmptyException();
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
