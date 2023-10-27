package kitchenpos.ordertable.domain;

import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(final Integer numberOfGuests, final Boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public void updateEmpty(boolean isEmpty) {
        this.empty = isEmpty;
    }

    public void updateTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void validateTableGroupIsNonNull() {
        if (!Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void validateNumberOfGuests() {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public void validateIsEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
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
