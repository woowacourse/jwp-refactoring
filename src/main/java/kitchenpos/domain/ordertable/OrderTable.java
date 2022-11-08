package kitchenpos.domain.ordertable;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.ordertable.validator.OrderTableValidator;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;

    @Column(name = "empty")
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        validateNumberOfGuests();
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        validateEmpty();
        validateNumberOfGuests();
    }

    private void validateEmpty() {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블은 손님 수를 변경할 수 없습니다.");
        }
    }

    private void validateNumberOfGuests() {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 음수가 될 수 없습니다.");
        }
    }

    public void changeEmpty(final boolean empty, final OrderTableValidator orderTableValidator) {
        orderTableValidator.validateAbleToChangeEmpty(empty, this);
        this.empty = empty;
    }

    public void group(final Long tableGroupId) {
        validateOrderTable();
        this.tableGroupId = tableGroupId;
        empty = false;
    }

    private void validateOrderTable() {
        if (!empty) {
            throw new IllegalArgumentException("그룹으로 지정할 테이블이 비어있지 않습니다.");
        }
        if (isGrouped()) {
            throw new IllegalArgumentException("그룹으로 지정할 테이블이 이미 그룹으로 지정되어 있습니다.");
        }
    }

    public void ungroup() {
        this.tableGroupId = null;
        empty = false;
    }

    public boolean isGrouped() {
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
}
