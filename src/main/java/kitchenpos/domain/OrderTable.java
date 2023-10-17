package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.exception.KitchenPosException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean empty;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = true)
    private Long tableGroupId;

    protected OrderTable() {
    }

    public OrderTable(Long id, boolean empty, int numberOfGuests) {
        validateNegativeNumberOfGuests(numberOfGuests);
        this.id = id;
        this.empty = empty;
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNegativeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new KitchenPosException("방문한 손님 수는 음수일 수 없습니다.");
        }
    }

    public void changeTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void changeEmpty(boolean empty) {
        if (tableGroupId != null) {
            throw new KitchenPosException("테이블 그룹에 속한 테이블은 상태를 변경할 수 없습니다.");
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateNegativeNumberOfGuests(numberOfGuests);
        if (empty) {
            throw new KitchenPosException("비어있는 상태의 테이블은 방문한 손님 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public boolean isEmpty() {
        return empty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
