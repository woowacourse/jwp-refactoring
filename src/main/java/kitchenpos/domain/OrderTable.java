package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(final int numberOfGuests, final boolean empty) {
        validateNegativeNumberOfGuests(numberOfGuests);

        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public void changeEmptyStatus(final boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("테이블 상태 변경을 위해선 테이블이 그룹으로 묶여있으면 안됩니다.");
        }
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        validateNegativeNumberOfGuests(numberOfGuests);

        if (isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있는 상태일 수 없습니다.");
        }

        this.numberOfGuests = numberOfGuests;
    }

    private static void validateNegativeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("음수로 주문 테이블의 손님 수를 변경할 수 없습니다.");
        }
    }
}
