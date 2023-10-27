package kitchenpos.table.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long groupId;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;


    public OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final Long groupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.groupId = groupId;
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
        if (!(this.groupId == null)) {
            throw new IllegalArgumentException("할당된 그룹이 존재합니다.");
        }
    }

    public void checkEmptyIsFalse() {
        if (this.empty) {
            throw new IllegalArgumentException("테이블의 상태가 비어 있습니다.");
        }
    }

    public void addGroup(final Long groupId) {
        checkTableGroupsEmpty();
        this.groupId = groupId;
    }

    public void unGroup() {
        this.groupId = null;
    }

    public void changeNumberOfGuests(final int number) {
        this.numberOfGuests = number;
    }

    public Long getId() {
        return id;
    }

    public Long getGroupId() {
        return groupId;
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
