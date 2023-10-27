package kitchenpos.ordertable;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.tablegroup.TableGroup;

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
        this.isEmpty = isEmpty;
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTable() {
        this(null, 0, false);
    }

    public OrderTable(int numberOfGuests, boolean isEmpty) {
        this(null, numberOfGuests, isEmpty);
    }

    public OrderTable(boolean isEmpty) {
        this(null, 0, isEmpty);
    }

    public Long getId() {
        return id;
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

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0 || this.isEmpty == true) {
            throw new IllegalArgumentException();
        }
    }

    public void setTableGroup(TableGroup tableGroup) {
        if (this.tableGroup != null || this.isEmpty == false) {
            throw new IllegalArgumentException();
        }
        this.tableGroup = tableGroup;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void ungroup() {
        this.tableGroup = null;
        this.isEmpty = false;
    }

    public void changeEmpty(boolean isEmpty) {
        if (isEmpty == true && Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
        this.isEmpty = isEmpty;
    }
}
