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

    private Long tableGroupId;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        validateIsGroupedTable();
        this.empty = empty;
    }

    private void validateIsGroupedTable() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("단체로 지정된 테이블은 상태를 변경할 수 없습니다.");
        }
    }

    public void updateNumberOfGuests(int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        validateChangeNumberOfGuestsWhenEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("1명 이상으로 변경할 수 있습니다.");
        }
    }

    private void validateChangeNumberOfGuestsWhenEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 손님 수 변경을 할 수 없습니다.");
        }
    }

    public void groupedBy(Long tableGroupId) {
        validateInvalidGrouping();
        this.empty = false;
        this.tableGroupId = tableGroupId;
    }

    private void validateInvalidGrouping() {
        if (!isEmpty() || Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("단체 지정이 불가능한 테이블입니다.");
        }
    }

    public void ungroup() {
        this.tableGroupId = null;
        changeEmpty(false);
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
