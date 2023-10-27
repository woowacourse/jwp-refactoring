package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("테이블 그룹에 포함된 테이블은 empty 상태를 변경할 수 없습니다.");
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("바꾸려는 손님 수는 0명 이상이어야 합니다.");
        }
        if (empty) {
            throw new IllegalArgumentException("빈 테이블은 손님 수를 바꿀 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void group(Long tableGroupId) {
        validateTableToGroup();
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    private void validateTableToGroup() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("이미 테이블 그룹에 포함된 테이블입니다.");
        }
        if (!empty) {
            throw new IllegalArgumentException("빈 테이블만 그룹화 할 수 있습니다.");
        }
    }

    public void unGroup() {
        this.tableGroupId = null;
        this.empty = false;
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
