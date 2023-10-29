package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    private TableGroup tableGroup;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        checkNumberOfGuestsSameOrGraterThanZero(numberOfGuests);
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private void checkNumberOfGuestsSameOrGraterThanZero(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void changeEmpty(final Boolean empty) {
        if (Objects.isNull(empty)) {
            throw new IllegalArgumentException();
        }
        checkTableGroupIsNull();
        this.empty = empty;
    }

    private void checkTableGroupIsNull() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
    }

    public void changeNumberOfGuests(final Integer numberOfGuests) {
        if (Objects.isNull(numberOfGuests)) {
            throw new IllegalArgumentException();
        }

        if (empty) {
            throw new IllegalArgumentException();
        }
        checkNumberOfGuestsSameOrGraterThanZero(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public boolean canBeGroup() {
        return empty && Objects.isNull(tableGroup);
    }

    public void registerGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        empty = false;
    }

    public void leaveGroup() {
        tableGroup = null;
        empty = false;
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
}
