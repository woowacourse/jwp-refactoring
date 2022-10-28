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

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void group(TableGroup tableGroup) {
        validateGroup();
        this.tableGroup = tableGroup;
        empty = false;
    }

    public void ungroup() {
        tableGroup = null;
        empty = false;
    }

    public void changeEmpty(boolean empty) {
        validateEmptyChangeable();
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateNumberOfGuestsChangeable(numberOfGuests);
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

    private void validateEmptyChangeable() {
        if (tableGroup != null) {
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

    private void validateGroup() {
        if (tableGroup != null) {
            throw new IllegalArgumentException("이미 다른 그룹에 존재하는 테이블입니다.");
        }

        if (!empty) {
            throw new IllegalArgumentException("비어있지 않은 테이블입니다.");
        }
    }
}
