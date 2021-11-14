package kitchenpos.order.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class OrderTable {

    @GeneratedValue
    @Id
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmptyStatus(final boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("테이블 그룹이 있는 상태에선 Empty 여부를 수정할 수 없습니다.");
        }
        this.empty = empty;
    }

    public void group(Long tableGroupId) {
        if (!isEmpty() || Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException("비어있지 않거나 이미 그룹이 있는 상태에선 그룹을 생성할 수 없습니다.");
        }
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("비어있는 상태에선 손님 수를 변경할 수 없습니다.");
        }

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("유효하지 않은 손님 수입니다.");
        }

        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public boolean isEmpty() {
        return empty;
    }
}
