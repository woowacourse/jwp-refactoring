package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TableGroup tableGroup;

    @Column
    private int numberOfGuests;

    @Column
    private boolean empty;

    protected OrderTable() {}

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void updateEmpty(final boolean empty) {
        if (isGrouped()) {
            throw new IllegalArgumentException("그룹에 속해있는 테이블은 빈 상태를 변경할 수 없습니다.");
        }
        this.empty = empty;
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public void groupBy(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public boolean isUnableToBeGrouped() {
        return !empty || Objects.nonNull(tableGroup);
    }

    private boolean isGrouped() {
        return Objects.nonNull(tableGroup);
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException("비어있는 테이블은 손님 수를 바꿀 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        if (tableGroup == null) {
            return null;
        }

        return tableGroup.getId();
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
