package kitchenpos.table.domain;

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

    protected OrderTable() {
    }

    public OrderTable(
            final Long tableGroupId,
            final int numberOfGuests,
            final boolean empty
    ) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(final boolean empty) {
        validateNoTableGroup();
        this.empty = empty;
    }

    public void validateNoTableGroup() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("테이블 그룹ID는 비어있어야 합니다.");
        }
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        validateEmptyTable();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("방문한 손님 수는 음수가 될 수 없습니다.");
        }
    }

    public void validateEmptyTable() {
        if (empty) {
            throw new IllegalArgumentException("주문 테이블은 비어있을 수 없습니다.");
        }
    }

    public void ungroup() {
        this.tableGroupId = null;
        this.empty = false;
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
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
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

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }
}
