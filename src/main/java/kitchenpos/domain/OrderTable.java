package kitchenpos.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Column
    private int numberOfGuests;

    @Column(name = "empty")
    private boolean isEmpty;

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean isEmpty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    public OrderTable() {
    }

    public OrderTable(boolean isEmpty) {
        this(null, 0, isEmpty);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Long getTableGroupId() {
        if (tableGroup == null) {
            return null;
        }
        return tableGroup.getId();
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(final boolean empty) {
        this.isEmpty = empty;
    }
}
