package kitchenpos.domain.ordertable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests = 0;
    private boolean empty = true;

    public OrderTable() {
    }

    public OrderTable(Long id) {
        this.id = id;
    }

    public void assign(Long tableGroupId) {
        validateEmpty();
        validateNotGrouped();
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroupId = null;
        this.empty = false;
    }

    public void empty() {
        validateNotGrouped();
        this.empty = true;
    }

    public void fill() {
        validateNotGrouped();
        this.empty = false;
    }

    public void fill(int numberOfGuests) {
        validateFull();
        validateNotNegative(numberOfGuests);
        this.empty = false;
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNotGrouped() {
        if (this.hasGroup()) {
            throw new IllegalArgumentException("그룹에 포함된 테이블입니다 tableId:" + id);
        }
    }

    private void validateFull() {
        if (isEmpty()) {
            throw new IllegalArgumentException("손님을 받기 전에 테이블을 먼저 채워주세요 tableId:" + id);
        }
    }

    private void validateEmpty() {
        if (isFull()) {
            throw new IllegalArgumentException("빈 테이블이 아닙니다 tableId:" + id);
        }
    }

    private void validateNotNegative(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 수는 양수여야 합니다");
        }
    }

    public boolean hasGroup() {
        return Objects.nonNull(tableGroupId);
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

    public boolean isFull() {
        return !empty;
    }
}
