package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

    private OrderTable(final Long id,
                       final TableGroup tableGroup,
                       final int numberOfGuests,
                       final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(final TableGroup tableGroup,
                                final int numberOfGuests,
                                final boolean empty) {
        return new OrderTable(null, tableGroup, numberOfGuests, empty);
    }

    public void updateTableGroup(final TableGroup group) {
        this.tableGroup = group;
        this.empty = false;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void updateEmpty(final boolean empty) {
        this.empty = empty;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
