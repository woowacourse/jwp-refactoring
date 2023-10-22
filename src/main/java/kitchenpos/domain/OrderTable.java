package kitchenpos.domain;

import java.util.Objects;

public class OrderTable {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean isEmpty;

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean isEmpty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean isEmpty) {
        this(null, tableGroupId, numberOfGuests, isEmpty);
    }

    public void changeIsEmpty(boolean hasCookingOrMealOrder, boolean isEmpty) {
        validateIsEmptyCanBeChanged(hasCookingOrMealOrder);
        this.isEmpty = isEmpty;
    }

    private void validateIsEmptyCanBeChanged(boolean hasCookingOrMealOrder) {
        if (isGrouped()) {
            throw new IllegalArgumentException("단체 지정된 주문 테이블은 비어있는지 여부를 변경할 수 없습니다.");
        }
        if (hasCookingOrMealOrder) {
            throw new IllegalArgumentException("조리 혹은 식사 중인 주문이 존재하는 주문 테이블은 비어있는지 여부를 변경할 수 없습니다.");
        }
    }

    public boolean isGrouped() {
        return Objects.nonNull(tableGroupId);
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateNumberOfGuestsCanBeChanged(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuestsCanBeChanged(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 0 미만일 수 없습니다.");
        }
        if (isEmpty) {
            throw new IllegalArgumentException("빈 주문 테이블에 손님 수를 변경할 수 없습니다.");
        }
    }

    public boolean isFilled() {
        return !isEmpty;
    }

    public void group(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.isEmpty = false;
    }

    public void ungroup() {
        this.tableGroupId = null;
        this.isEmpty = false;
    }

    public void validateTableCanTakeOrder() {
        if (isEmpty) {
            throw new IllegalArgumentException("빈 주문 테이블은 주문을 받을 수 없습니다");
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
        return isEmpty;
    }
}
