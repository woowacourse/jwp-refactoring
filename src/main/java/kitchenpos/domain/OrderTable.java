package kitchenpos.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
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
        return !empty || Objects.nonNull(tableGroup);
    }

    public void registerGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void leaveGroup() {
        tableGroup = null;
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
