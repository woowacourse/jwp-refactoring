package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    private TableGroup tableGroup;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(name = "empty", nullable = false)
    private boolean isEmpty;

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean isEmpty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean isEmpty) {
        this(null, tableGroup, numberOfGuests, isEmpty);
    }

    protected OrderTable() {
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
        return Objects.nonNull(tableGroup);
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

    public void group(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.isEmpty = false;
    }

    public void ungroup() {
        this.tableGroup = null;
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

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
