package kitchenpos.table.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class OrderTable {

    private static final int MIN_NUMBER_OF_GUESTS = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    @NotNull
    private Integer numberOfGuests;

    @NotNull
    private Boolean empty;

    protected OrderTable() {
    }

    private OrderTable(Integer numberOfGuests, Boolean empty) {
        validateNumberOfGuests(numberOfGuests);
        validateEmpty(empty);

        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private void validateNumberOfGuests(Integer numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException("테이블에 방문한 손님 수는 0 이상이어야 합니다.");
        }
    }

    private void validateEmpty(Boolean empty) {
        if (empty == null) {
            throw new NullPointerException("empty는 null일 수 없습니다.");
        }
    }

    public static OrderTable create(Integer numberOfGuests, Boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public void changeNumberOfGuests(Integer numberOfGuests) {
        validateChangeableNumberOfGuests(numberOfGuests);

        this.numberOfGuests = numberOfGuests;
    }

    private void validateChangeableNumberOfGuests(Integer numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);

        if (Boolean.TRUE.equals(isEmpty())) {
            throw new IllegalArgumentException("주문을 할 수 없는 상태이므로, 방문 손님 수를 변경할 수 없습니다.");
        }
    }

    public void changeEmpty(Boolean empty, OrderTableValidator orderTableValidator) {
        validateChangeableEmpty();
        orderTableValidator.validateChangeableEmpty(id);

        this.empty = empty;
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
        this.empty = Boolean.FALSE;
    }

    public void ungroup() {
        this.tableGroupId = null;
        this.empty = Boolean.FALSE;
    }

    public boolean isGrouped() {
        return tableGroupId != null;
    }

    public Long getId() {
        return id;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean isEmpty() {
        return empty;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

}
