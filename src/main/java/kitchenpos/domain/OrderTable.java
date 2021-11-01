package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id) {
        this(id, null, 0, false);
    }

    public OrderTable(boolean empty) {
        this(null, null, null, empty);
    }

    public OrderTable(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public void enrollTableGroup(TableGroup savedTableGroup) {
        this.tableGroup = tableGroup;
    }

    public void grouped(boolean empty) {
        this.empty = empty;
    }

    public void releaseTableGroup() {
        this.tableGroup = null;
    }

    public void enrollId(Long id) {
        this.id = id;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
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

    public Long getTableGroupId() {
        return tableGroup.getId();
    }
}
