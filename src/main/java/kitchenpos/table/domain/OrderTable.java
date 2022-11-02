package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
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

    public static OrderTable createWithoutTableGroup(int numberOfGuests, boolean empty) {
        return new OrderTable(null, numberOfGuests, empty);
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

    public void changeEmptyStatus(final boolean changedEmptyStatus) {
        if (Objects.nonNull(getTableGroup())) {
            throw new IllegalArgumentException();
        }
        empty = changedEmptyStatus;
    }

    public boolean existsTableGroup() {
        return Objects.nonNull(tableGroup);
    }

    public void changeNumberOfGuests(final int changedNumberOfGuests) {
        validateChangeGuests(changedNumberOfGuests);

        numberOfGuests = changedNumberOfGuests;
    }

    private void validateChangeGuests(final int changedNumberOfGuests) {
        if (changedNumberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    public void ungroup() {
        tableGroup = null;
        empty = false;
    }

    public void changeTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }
}
