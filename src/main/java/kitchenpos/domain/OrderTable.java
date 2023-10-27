package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

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
        this.tableGroupId = null;
        this.empty = false;
    }

    public void groupBy(final TableGroup tableGroup) {
        this.tableGroupId = tableGroup.getId();
    }

    public boolean isUnableToBeGrouped() {
        return !empty || Objects.nonNull(tableGroupId);
    }

    private boolean isGrouped() {
        return Objects.nonNull(tableGroupId);
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateGuestChange(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateGuestChange(final int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException("비어있는 테이블은 손님 수를 바꿀 수 없습니다.");
        }
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("변경하려는 손님 수는 0보다 작을 수 없습니다.");
        }
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
}
