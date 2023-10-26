package kitchenpos.domain;

import kitchenpos.domain.vo.GuestsNumber;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TableGroup tableGroup;

    @Embedded
    private GuestsNumber numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = new GuestsNumber(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = new GuestsNumber(numberOfGuests);
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void ungroupById(final long tableGroupId) {
        if (tableGroup.getId().equals(tableGroupId)) {
            tableGroup = null;
            empty = false;
        }
    }

    public void group(final TableGroup tableGroup) {
        this.empty = false;
        this.tableGroup = tableGroup;
    }

    public void setEmpty(final boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
        this.empty = empty;
    }

    public void setTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        final GuestsNumber guestsNumber = new GuestsNumber(numberOfGuests);
        this.numberOfGuests = guestsNumber;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
