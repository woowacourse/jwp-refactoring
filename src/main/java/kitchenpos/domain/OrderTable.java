package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long id) {
        this(id, null, 0, true);
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void validateEmpty() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    public void validateTableGroup() {
        if (empty || Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
    }

    public void validateGuest() {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void clearTableGroup() {
        tableGroup = null;
    }

    public Long tableGroupId() {
        return tableGroup.getId();
    }

    public Long getId() {
        return id;
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

    public boolean isEmpty() {
        return empty;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
