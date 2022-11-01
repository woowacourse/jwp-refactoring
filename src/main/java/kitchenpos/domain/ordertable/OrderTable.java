package kitchenpos.domain.ordertable;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    private static final int MIN_NUMBER_OF_GUESTS = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable ofNew(final int numberOfGuests, final boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public void joinGroup(final TableGroup tableGroup) {
        checkEmpty();
        checkNotGrouped();
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroup = null;
        this.empty = true;
    }

    public void acceptGuests(final int numberOfGuests) {
        checkMinGuests(numberOfGuests);
        checkNotEmpty();
        acceptGuests();
        this.numberOfGuests = numberOfGuests;
    }

    public void acceptGuests() {
        checkNotGrouped();
        this.empty = false;
    }

    public void clear() {
        checkNotGrouped();
        this.empty = true;
    }

    private void checkMinGuests(final int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException();
        }
    }

    private void checkNotGrouped() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
    }

    private void checkEmpty() {
        if (!empty) {
            throw new IllegalArgumentException();
        }
    }

    private void checkNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        if (tableGroup == null) {
            return null;
        }
        return tableGroup.getId();
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    protected OrderTable() {
    }
}
