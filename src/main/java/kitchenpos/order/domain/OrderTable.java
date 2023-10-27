package kitchenpos.order.domain;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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

    public void updateEmptyIfNoConstraints(final boolean empty, final OrderTableValidator orderTableValidator) {
        validateTableGroupEmpty();
        orderTableValidator.validateOrderStatus(this);
        this.empty = empty;
    }

    private void validateTableGroupEmpty() {
        if (nonNull(tableGroupId)) {
            throw new IllegalArgumentException("단체 지정이 되어있습니다.");
        }
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("올바르지 않은 숫자입니다.");
        }
        if (empty) {
            throw new IllegalArgumentException("빈 테이블은 게스트 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void groupBy(final long tableGroupId) {
        validateTableStatus();
        this.empty = false;
        this.tableGroupId = tableGroupId;
    }

    private void validateTableStatus() {
        if (!isEmpty() || nonNull(tableGroupId)) {
            throw new IllegalArgumentException("단체 지정이 불가능한 테이블이 포함되어 있습니다.");
        }
    }

    public void ungroupBy(final long tableGroupId, final OrderTableValidator orderTableValidator) {
        orderTableValidator.validateUngrouping(this);
        validateRightTableGroup(tableGroupId);
        this.empty = false;
        this.tableGroupId = null;
    }

    private void validateRightTableGroup(final long tableGroupId) {
        if(this.tableGroupId != tableGroupId) {
            throw new IllegalArgumentException("해당 단체에 지정되어 있지 않아 단체 지정 해제가 불가능합니다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
