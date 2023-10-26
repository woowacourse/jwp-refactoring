package kitchenpos.domain.table;

import javax.persistence.*;

@Entity
public class OrderTable {

    private static final int MIN_NUMBER_OF_GUESTS = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        validateNumberOfGuests(numberOfGuests);

        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final int numberOfGuests) {
        this(null, numberOfGuests, true);
    }

    protected OrderTable() {
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException();
        }
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

    public void setNumberOfGuests(final int numberOfGuests) {
        validateIsEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateIsEmpty() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }
}
