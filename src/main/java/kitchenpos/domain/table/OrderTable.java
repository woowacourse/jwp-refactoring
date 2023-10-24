package kitchenpos.domain.table;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.domain.tablegroup.TableGroup;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }


    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void updateEmptyStatus(final boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("그룹화 된 테이블은 못비워용");
        }
        this.empty = empty;
    }

    public void updateNumberOfGuest(final int numberOfGuests) {
        if(numberOfGuests < 0) {
            throw new IllegalArgumentException("손님이 0명 이하에요");
        }

        if (empty) {
            throw new IllegalArgumentException("테이블이 비어있어요");
        }

        this.numberOfGuests = numberOfGuests;
    }


    public void changeGroup(final TableGroup tableGroup) {
        this.empty = false;
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        tableGroup.remove(this);
        this.tableGroup = null;
        this.empty = false;
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
