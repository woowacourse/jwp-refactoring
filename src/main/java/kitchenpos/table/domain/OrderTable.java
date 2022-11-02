package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.Hibernate;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void group(Long tableGroupId) {
        validateGroup();
        this.tableGroupId = tableGroupId;
        empty = false;
    }

    public void ungroup() {
        tableGroupId = null;
        empty = false;
    }

    public void changeEmpty(boolean empty) {
        validateChangeEmpty();
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateNumberOfGuestsChangeable(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    private void validateGroup() {
        if (tableGroupId != null) {
            throw new IllegalArgumentException("이미 다른 그룹에 존재하는 테이블입니다.");
        }

        if (!empty) {
            throw new IllegalArgumentException("비어있지 않은 테이블입니다.");
        }
    }

    private void validateChangeEmpty() {
        if (tableGroupId != null) {
            throw new IllegalArgumentException("테이블 그룹에 묶여있어 상태를 변경할 수 없습니다.");
        }
    }

    private void validateNumberOfGuestsChangeable(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 0 이상이어야 합니다.");
        }

        if (empty) {
            throw new IllegalArgumentException("비어있는 테이블입니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        OrderTable orderTable = (OrderTable)o;
        return id != null && Objects.equals(id, orderTable.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
