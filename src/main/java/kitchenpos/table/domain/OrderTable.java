package kitchenpos.table.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.tablegroup.domain.TableGroup;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;


    public OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, numberOfGuests, empty);
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

    public void checkEmptyAndTableGroups() {
        if (!this.empty) {
            throw new IllegalArgumentException("테이블의 상태가 비어 있지 않습니다.");
        }
        checkTableGroupsEmpty();
    }

    public void checkTableGroupsEmpty() {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException("할당된 그룹이 존재합니다.");
        }
    }

    public void checkEmptyIsFalse() {
        if (this.empty) {
            throw new IllegalArgumentException("테이블의 상태가 비어 있습니다.");
        }
    }

    public void addGroup(final TableGroup group) {
        checkTableGroupsEmpty();
        this.tableGroup = group;
    }

    public void unGroup() {
        this.tableGroup = null;
    }

    public void changeNumberOfGuests(final int number) {
        this.numberOfGuests = number;
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

    public void changeEmpty(final boolean status) {
        this.empty = status;
    }
}
