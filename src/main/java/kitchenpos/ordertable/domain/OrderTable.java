package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.ordertable.exception.OrderTableException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void updateTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public boolean isExistTableGroup() {
        return Objects.nonNull(tableGroup);
    }

    public void unGroup() {
        this.tableGroup = null;
        this.empty = false;
    }

    public void changeEmptyStatus(final boolean isEmpty) {
        this.empty = isEmpty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateAvailableChangeNumberOfGuests();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateAvailableChangeNumberOfGuests() {
        if (this.empty == true) {
            throw new OrderTableException.CannotChangeNumberOfGuestsStateInEmptyException();
        }
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
