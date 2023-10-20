package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.table.exception.NotEnoughGuestsException;
import kitchenpos.table.exception.OrderTableEmptyException;
import kitchenpos.table.exception.OrderTableNotEmptyException;
import kitchenpos.tablegroup.exception.TableGroupExistsException;

@Table(name = "order_table")
@Entity
public class OrderTable {

    private static final int NUMBER_OF_GUESTS_LOWER_LIMIT = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeTableGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void changeEmpty(boolean empty) {
        validateTableGroupNotExists();
        this.empty = empty;
    }

    public void validateTableGroupNotExists() {
        if (tableGroupId != null) {
            throw new TableGroupExistsException();
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateIsNotEmpty();
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public void validateIsNotEmpty() {
        if (isEmpty()) {
            throw new OrderTableEmptyException();
        }
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < NUMBER_OF_GUESTS_LOWER_LIMIT) {
            throw new NotEnoughGuestsException();
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

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
