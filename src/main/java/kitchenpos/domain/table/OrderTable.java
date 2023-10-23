package kitchenpos.domain.table;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.domain.exception.OrderTableException.NotEnoughGuestsException;
import kitchenpos.domain.exception.TableGroupException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numberOfGuests;

    private boolean empty;

    @ManyToOne
    private TableGroup tableGroup;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static OrderTable of(final int numberOfGuests) {
        return new OrderTable(numberOfGuests);
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new TableGroupException.GroupAlreadyExistsException();
        }
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void changeGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new NotEnoughGuestsException();
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void changeTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        this.tableGroup = null;
    }
}
