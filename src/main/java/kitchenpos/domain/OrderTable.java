package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private TableGroup tableGroup;

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

    public void changeTableGroup(TableGroup tableGroup) {
        if (empty) {
            throw new KitchenPosException("비어있는 상태의 주문 테이블은 테이블 그룹에 등록할 수 없습니다.");
        }
        if (this.tableGroup != null) {
            throw new KitchenPosException("이미 테이블 그룹에 속해있는 주문 테이블 입니다.");
        }
        this.tableGroup = tableGroup;
    }

    public void changeEmpty(boolean empty) {
        if (tableGroup != null) {
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

    public void ungroup() {
        this.tableGroup = null;
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
        if (tableGroup == null) {
            return null;
        }
        return tableGroup.getId();
    }
}
