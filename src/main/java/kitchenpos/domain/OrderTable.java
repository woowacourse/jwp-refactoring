package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.exception.OrderTableException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {}

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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

    public boolean isEmpty() {
        return empty;
    }

    public boolean isExistTableGroup() {
        return Objects.nonNull(this.tableGroup);
    }

    public void confirmTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        tableGroup.getOrderTables().add(this);
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
}
