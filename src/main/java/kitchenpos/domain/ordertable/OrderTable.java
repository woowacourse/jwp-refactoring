package kitchenpos.domain.ordertable;

import java.util.Objects;
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
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;

    @Column(name = "empty")
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long id, final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        validateNumberOfGuests(numberOfGuests);
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateEmpty();
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateEmpty() {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블은 손님 수를 변경할 수 없습니다.");
        }
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 음수가 될 수 없습니다.");
        }
    }

    public void changeEmpty(final boolean empty) {
        validateChangePossible(empty);
        this.empty = empty;
    }

    private void validateChangePossible(final boolean empty) {
        if (empty && Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("그룹화된 테이블은 빈 상태로 변경할 수 없습니다.");
        }
    }

    public void changeTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        empty = false;
    }

    public void ungroup() {
        this.tableGroup = null;
        empty = false;
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

    public void setTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
