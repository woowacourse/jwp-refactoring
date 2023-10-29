package kitchenpos.table.domain;

import javax.persistence.Embedded;
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

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Embedded
    private Empty empty;

    protected OrderTable() {
    }

    private OrderTable(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
        this.empty = Empty.from(empty);
    }

    public static OrderTable create(Integer numberOfGuests, Boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public void changeNumberOfGuests(Integer numberOfGuests) {
        validateChangeableNumberOfGuests();

        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
    }

    private void validateChangeableNumberOfGuests() {
        if (Boolean.TRUE.equals(isEmpty())) {
            throw new IllegalArgumentException("주문을 할 수 없는 상태이므로, 방문 손님 수를 변경할 수 없습니다.");
        }
    }

    public void changeEmpty(Boolean empty, OrderTableValidator orderTableValidator) {
        validateChangeableEmpty();
        orderTableValidator.validateChangeableEmpty(id);

        this.empty = Empty.from(empty);
    }

    private void validateChangeableEmpty() {
        if (isGrouped()) {
            throw new IllegalArgumentException("다른 그룹에 속해있으므로, 주문 상태를 변경할 수 없습니다.");
        }
    }

    public void group(Long tableGroupId) {
        if (isGrouped()) {
            throw new IllegalArgumentException("이미 다른 그룹에 속한 테이블입니다.");
        }

        this.tableGroupId = tableGroupId;
        this.empty = Empty.from(Boolean.FALSE);
    }

    public void ungroup() {
        this.tableGroupId = null;
        this.empty = Empty.from(Boolean.FALSE);
    }

    public boolean isGrouped() {
        return tableGroupId != null;
    }

    public Long getId() {
        return id;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public Boolean isEmpty() {
        return empty.isEmpty();
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

}
