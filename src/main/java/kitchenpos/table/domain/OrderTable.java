package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.table.exception.CannotChangeEmptyTableNumberOfGuestsException;
import kitchenpos.table.exception.CannotChangeGroupedTableEmptyException;
import kitchenpos.table.exception.OrderTableCannotBeGroupedException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Column(name = "number_of_guests")
    private int numberOfGuests;

    private boolean empty;

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    protected OrderTable() {
    }

    public boolean isEmpty() {
        return empty;
    }

    public Long getId() {
        return id;
    }

    public void addToTableGroup(Long tableGroupId) {
        validateCanBeAddedToNewTableGroup();
        this.tableGroupId = tableGroupId;
    }

    private void validateCanBeAddedToNewTableGroup() {
        if (isEmpty() || Objects.nonNull(tableGroupId)) {
            throw new OrderTableCannotBeGroupedException();
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (isEmpty()) {
            throw new CannotChangeEmptyTableNumberOfGuestsException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new CannotChangeGroupedTableEmptyException();
        }
        this.empty = empty;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
