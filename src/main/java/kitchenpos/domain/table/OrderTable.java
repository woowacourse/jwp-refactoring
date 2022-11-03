package kitchenpos.domain.table;

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
    @Column(name = "table_group_id", insertable = false, updatable = false)
    private Long tableGroupId;
    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;
    @Column(name = "empty", nullable = false)
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

    public void changeEmpty(final boolean empty) {
        if (Objects.nonNull(this.getTableGroupId())) {
            throw new IllegalArgumentException("테이블 그룹이 있어 주문 테이블 상태를 변경할 수 없습니다.");
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateEmpty();
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateEmpty() {
        if (this.empty) {
            throw new IllegalArgumentException("주문 테이블은 비어있을 수 없습니다.");
        }
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("주문 테이블의 손님의 수는 0명 미만일 수 없습니다.");
        }
    }

    public boolean isUsing() {
        return !empty || Objects.nonNull(tableGroupId);
    }

    public void unBindGroup() {
        this.tableGroupId = null;
        this.empty = false;
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
